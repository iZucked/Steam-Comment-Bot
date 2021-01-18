/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util;

import java.io.CharConversionException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.IOWrappedException;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter.Cipher;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.IllegalValueException;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scenario.service.model.util.encryption.ScenarioEncryptionException;
import com.mmxlabs.scenario.service.model.util.encryption.impl.PassthroughCipherProvider;

public final class ResourceHelper {

	@SuppressWarnings({ "unused", "null" })
	public static @NonNull ResourceSet createResourceSet(@Nullable IScenarioCipherProvider scenarioCipherProvider) {
		final ResourceSet resourceSet = new ResourceSetImpl();

		// Fall back
		if (scenarioCipherProvider == null) {
			scenarioCipherProvider = new PassthroughCipherProvider();
		}

		// Encryption hooks
		if (true) {
			if (scenarioCipherProvider == null) {
				throw new RuntimeException("No cipher provided, unable to load or save scenarios");
			}
			final Cipher cipher = scenarioCipherProvider.getSharedCipher();
			if (cipher == null) {
				throw new RuntimeException("No cipher provided, unable to load or save scenarios");
			}
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new EncryptingXMIResourceFactory(cipher));
		} else {
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		}
		// Set Default Load Options
		{
			resourceSet.getLoadOptions().put(XMLResource.OPTION_DEFER_ATTACHMENT, Boolean.TRUE);
			resourceSet.getLoadOptions().put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
			resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl(true));
			resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap<Object, Object>());
		}
		// TODO: Set Default Save Options
		return resourceSet;
	}

	public static Resource loadResource(@NonNull final ResourceSet resourceSet, @NonNull final URI uri) throws IOException {

		// Check for existing resource before creating otherwise we can get multiple resources for the same URI
		Resource resource = resourceSet.getResource(uri, false);
		if (resource == null) {
			resource = resourceSet.createResource(uri);
			if (resource instanceof ResourceImpl) {
				// This helps speed up model loading
				final HashMap<String, EObject> intrinsicIDToEObjectMap = new HashMap<>();
				((ResourceImpl) resource).setIntrinsicIDToEObjectMap(intrinsicIDToEObjectMap);
			}
		}
		Map<Object, Object> loadOptions = new HashMap<>(resourceSet.getLoadOptions());
		boolean disabledCipher = false;
		boolean disabledIDMap = false;

		while (true) {
			try {
				// Attempt to load using default options
				resource.load(loadOptions);
				break;
			} catch (IOWrappedException ee) {
				if (ee.getCause() instanceof IllegalValueException) {
					// This can be triggered by multiple entries for the same object instance in a unique list
					if (!disabledIDMap) {
						disabledIDMap = true;
						loadOptions.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.FALSE);
						resource.unload();

						continue;
					}
				}
				if (ee.getCause() instanceof ScenarioEncryptionException) {
					if (!disabledCipher) {
						disabledCipher = true;
						loadOptions.put(Resource.OPTION_CIPHER, null);
						resource.unload();
						continue;
					}
				}
				throw ee;
			} catch (CharConversionException /* MalformedByteSequenceException */ e2) {
				if (!disabledCipher) {
					disabledCipher = true;
					loadOptions.put(Resource.OPTION_CIPHER, null);
					resource.unload();
					continue;
				}
				throw e2;
			} catch (final Exception e) {
				// Unable to load using fallback, so rethrow original exception
				throw e;
			}
		}
		return resource;
	}

	public static void saveResource(@NonNull final Resource resource) throws IOException {
		assert resource.getContents().size() > 0;
		resource.save(null);
	}

	public static Resource createResource(final ResourceSet resourceSet, final URI uri) {
		final Resource resource = resourceSet.createResource(uri);
		if (resource instanceof ResourceImpl) {
			// This helps speed up model loading
			final HashMap<String, EObject> intrinsicIDToEObjectMap = new HashMap<String, EObject>();
			((ResourceImpl) resource).setIntrinsicIDToEObjectMap(intrinsicIDToEObjectMap);
		}
		return resource;
	}
}
