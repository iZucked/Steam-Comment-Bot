/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.manifest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteStreams;
import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;

/**
 * Utility class for getting a scenario out into a zipfile or vice-versa
 * 
 * @author hinton
 * 
 */
public class ScenarioStorageUtil {

	private static final Logger log = LoggerFactory.getLogger(ScenarioStorageUtil.class);

	protected File lastTemporaryFile;

	// protected File getTemporaryFile(final ScenarioInstance instance) {
	// final String name = instance.getName();
	// final String uuid = instance.getUuid();
	//
	// final File uuidDir = new File(storageDirectory.toFile(), escape(uuid) + ".d");
	// if (uuidDir.exists() == false) {
	// uuidDir.mkdirs();
	// }
	// return new File(uuidDir, escape(name) + ".lingo");
	// }

	private static String escape(final String name) {
		return name.replaceAll("[\\W&&[^ ]]+", "-");
	}

	public static File storeToTemporaryFile(final ScenarioInstance instance) throws IOException {
		final File tempFile = File.createTempFile(escape(instance.getUuid()), ".lingo");

		tempFile.deleteOnExit();
		storeToFile(instance, tempFile);

		return tempFile;
	}

	public static void storeToFile(final ScenarioInstance instance, final File file) throws IOException {
		final ResourceSetImpl resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());

		final IScenarioService scenarioService = instance.getScenarioService();

		final URI rootObjectURI = URI.createURI("archive:" + URI.createFileURI(file.getAbsolutePath()) + "!/rootObject.xmi");

		final EObject model = instance.getInstance();
		// Already loaded?
		if (model != null) {
			final EObject rootObject = EcoreUtil.copy(model);
			final XMIResourceImpl r = new XMIResourceImpl(rootObjectURI);
			r.getContents().add(rootObject);
			final Map<Object, Object> saveOptions = new HashMap<>();
			// Force default values and types to be saved
			saveOptions.put(XMLResource.OPTION_KEEP_DEFAULT_CONTENT, Boolean.TRUE);
			saveOptions.put(XMLResource.OPTION_SAVE_TYPE_INFORMATION, Boolean.TRUE);
			// Save the model.
			r.save(saveOptions);
		} else {
			// Store data into scenario archive
			final URIConverter conv = resourceSet.getURIConverter();
			final URI u = scenarioService.resolveURI(instance.getRootObjectURI());

			OutputStream output = null;
			InputStream input = null;
			try {
				input = conv.createInputStream(u);
				output = conv.createOutputStream(rootObjectURI);

				ByteStreams.copy(input, output);
				output.flush();
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (final IOException e) {

					}
				}
				if (output != null) {
					try {
						output.close();
					} catch (final IOException e) {

					}
				}
			}
		}

		// Now store the metadata
		{
			final Manifest manifest = ManifestFactory.eINSTANCE.createManifest();
			manifest.setScenarioType(instance.getMetadata().getContentType());
			manifest.setUUID(instance.getUuid());
			manifest.setScenarioVersion(instance.getScenarioVersion());
			manifest.setVersionContext(instance.getVersionContext());
			final URI manifestURI = URI.createURI("archive:" + URI.createFileURI(file.getAbsolutePath()) + "!/MANIFEST.xmi");
			final Resource manifestResource = resourceSet.createResource(manifestURI);

			manifestResource.getContents().add(manifest);

			manifest.getModelURIs().add("rootObject.xmi");
			manifestResource.save(null);
		}

	}

	/**
	 * @param filePath
	 * @return
	 */
	public static ScenarioInstance loadInstanceFromFile(final String filePath) {
		return loadInstanceFromURI(URI.createFileURI(filePath), false);
	}

	/**
	 * @param filePath
	 * @param preLoad
	 *            If true, load the resources and hook up submodels. If false, just hook up the URIs. Use false if this will be used in the {@link IScenarioService} / ModelService context
	 * @return
	 */
	public static ScenarioInstance loadInstanceFromURI(final URI scenarioURI, final boolean preLoad) {
		final URI manifestURI = URI.createURI("archive:" + scenarioURI.toString() + "!/MANIFEST.xmi");
		final ResourceSetImpl resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		resourceSet.getLoadOptions().put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, true);
		resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl(true));
		resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap<Object, Object>());

		final HashMap<String, EObject> intrinsicIDToEObjectMap = new HashMap<String, EObject>();

		final Resource resource = resourceSet.createResource(manifestURI);
		try {
			resource.load(null);
			if (resource.getContents().size() == 1) {
				final EObject top = resource.getContents().get(0);
				if (top instanceof Manifest) {
					final Manifest manifest = (Manifest) top;
					final ScenarioInstance result = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
					final Metadata meta = ScenarioServiceFactory.eINSTANCE.createMetadata();

					result.setMetadata(meta);

					result.setName(scenarioURI.trimFileExtension().lastSegment());
					result.setUuid(manifest.getUUID());

					result.setVersionContext(manifest.getVersionContext());
					result.setScenarioVersion(manifest.getScenarioVersion());

					meta.setContentType(manifest.getScenarioType());

					for (final String smu : manifest.getModelURIs()) {
						URI rel = URI.createURI(smu);
						if (rel.isRelative()) {
							rel = rel.resolve(manifestURI);
						}
						result.setRootObjectURI(rel.toString());
						break;
					}

					if (preLoad) {
						final MMXRootObject implementation = MMXCoreFactory.eINSTANCE.createMMXRootObject();
						result.setInstance(implementation);

						final Resource r = resourceSet.createResource(URI.createURI(result.getRootObjectURI()));
						if (r instanceof ResourceImpl) {
							((ResourceImpl) r).setIntrinsicIDToEObjectMap(intrinsicIDToEObjectMap);
						}
						r.load(null);
					}

					return result;
				}
			}
		} catch (final Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
}
