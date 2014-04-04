package com.mmxlabs.scenario.service.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter.Cipher;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scenario.service.util.encryption.IScenarioCipherProvider;

public final class ResourceHelper {

	public static ResourceSet createResourceSet(final IScenarioCipherProvider scenarioCipherProvider) {
		final ResourceSet resourceSet = new ResourceSetImpl();
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
			resourceSet.getLoadOptions().put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, true);
			resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl(true));
			resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap<Object, Object>());
		}
		// TODO: Set Default Save Options
		return resourceSet;
	}

	public static Resource loadResource(@NonNull final ResourceSet resourceSet, @NonNull final URI uri) throws IOException {

		final Resource resource = resourceSet.createResource(uri);
		if (resource instanceof ResourceImpl) {
			// This helps speed up model loading
			final HashMap<String, EObject> intrinsicIDToEObjectMap = new HashMap<String, EObject>();
			((ResourceImpl) resource).setIntrinsicIDToEObjectMap(intrinsicIDToEObjectMap);
		}
		try {
			// Attempt to load using default options
			resource.load(null);
		} catch (final Exception e) {
			// Need to unload the model, even if it failed otherwise it is still flagged up as loaded
			resource.unload();
			// Fall back to pre-encrypted scenario options
			final Map<Object, Object> noCipherOptions = new HashMap<>();
			noCipherOptions.put(Resource.OPTION_CIPHER, null);
			try {
				resource.load(noCipherOptions);
			} catch (final Exception e2) {
				// Unable to load using fallback, so rethrow original exception
				throw e;
			}

		}
		return resource;
	}

	public static void saveResource(@NonNull final Resource resource) throws IOException {
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
