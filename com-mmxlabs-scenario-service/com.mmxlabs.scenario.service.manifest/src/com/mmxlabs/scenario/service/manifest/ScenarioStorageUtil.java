/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.manifest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.mmxcore.util.MMXCoreBinaryResourceFactoryImpl;
import com.mmxlabs.models.mmxcore.util.MMXCoreHandlerUtil;
import com.mmxlabs.models.mmxcore.util.MMXCoreResourceFactoryImpl;
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

	static final ScenarioStorageUtil INSTANCE = new ScenarioStorageUtil();
	protected File storageDirectory;
	protected File lastTemporaryFile;

	protected ScenarioStorageUtil() {
		try {
			storageDirectory = File.createTempFile("ScenarioStorage", "dir");
			// there is a race here; the only way to really avoid it is to use
			// something like java.nio in java 7, which has a createTempDir method.
			if (storageDirectory.delete()) {
				storageDirectory.mkdir();
			}
			// TODO should we delete this on finalize? if the user has copied something she would expect
			// it to remain copied so perhaps we should delete all but the most recent copied thing?
			// Is that a job for the copy handler anyway?
		} catch (final IOException e) {
			storageDirectory = null;
		}
	}

	protected File getTemporaryFile(final ScenarioInstance instance) {
		final String name = instance.getName();
		final String uuid = instance.getUuid();

		final File uuidDir = new File(storageDirectory, escape(uuid) + ".d");
		if (uuidDir.exists() == false) {
			uuidDir.mkdirs();
		}
		return new File(uuidDir, escape(name) + ".scenario");
	}

	private String escape(final String name) {
		return name.replaceAll("[\\W&&[^ ]]+", "-");
	}

	public static String storeToTemporaryFile(final ScenarioInstance instance) throws IOException {
		final File tempFile = INSTANCE.getTemporaryFile(instance);

		tempFile.deleteOnExit();
		storeToFile(instance, tempFile);

		return tempFile.getAbsolutePath();
	}

	public static void storeToFile(final ScenarioInstance instance, final File file) throws IOException {
		final ResourceSetImpl resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new MMXCoreResourceFactoryImpl());
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmb", new MMXCoreBinaryResourceFactoryImpl());
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		final Manifest manifest = ManifestFactory.eINSTANCE.createManifest();
		manifest.setScenarioType(instance.getMetadata().getContentType());
		manifest.setUUID(instance.getUuid());
		final URI manifestURI = URI.createURI("archive:" + URI.createFileURI(file.getAbsolutePath()) + "!/MANIFEST.xmi");
		final Resource manifestResource = resourceSet.createResource(manifestURI);

		manifestResource.getContents().add(manifest);

		final IScenarioService scenarioService = instance.getScenarioService();
		final List<String> partURIs = new ArrayList<String>();

		addURIs(partURIs, instance, scenarioService);

		final URIConverter conv = resourceSet.getURIConverter();

		int index = 0;
		final byte[] buffer = new byte[10 * 4096];
		// long l = System.currentTimeMillis();
		//
		// int index = 0;
		// for (final String partURI : partURIs) {
		// final URI u = URI.createURI(partURI);
		// final Resource r = resourceSet.createResource(u);
		// r.load(null);
		// if (!r.getContents().isEmpty()) {
		// final EObject top = r.getContents().get(0);
		// final URI relativeURI = URI.createURI("/" + top.eClass().getName() + index++ + ".xmi");
		// manifest.getModelURIs().add(relativeURI.toString());
		// final URI resolved = relativeURI.resolve(manifestURI);
		// final Resource r2 = resourceSet.createResource(resolved);
		// r2.getContents().add(top);
		// r2.save(null);
		// }
		// }

		for (final String partURI : partURIs) {
			final URI u = scenarioService.resolveURI(partURI);
			final InputStream input = conv.createInputStream(u);
			final URI relativeURI = URI.createURI("/" + index++ + "-" + u.segment(u.segmentCount() - 1));
			manifest.getModelURIs().add(relativeURI.toString());
			final URI resolved = relativeURI.resolve(manifestURI);
			final OutputStream output = conv.createOutputStream(resolved);
			try {
				int b;
				while ((b = input.read(buffer)) != -1) {
					output.write(buffer, 0, b);
				}

				output.flush();
			} finally {
				output.close();
			}
		}
		// System.err.println("time: " + (System.currentTimeMillis() - l));
		manifestResource.save(null);
	}

	private static void addURIs(final List<String> partURIs, final ScenarioInstance instance, final IScenarioService scenarioService) {
		if (instance == null)
			return;
		partURIs.addAll(instance.getSubModelURIs());
		for (final String depUUID : instance.getDependencyUUIDs()) {
			addURIs(partURIs, scenarioService.getScenarioInstance(depUUID), scenarioService);
		}
	}

	/**
	 * @param filePath
	 * @return
	 */
	public static ScenarioInstance loadInstanceFromFile(final String filePath) {
		return loadInstanceFromURI(URI.createFileURI(filePath) , false);
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
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new MMXCoreResourceFactoryImpl());
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmb", new MMXCoreBinaryResourceFactoryImpl());
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());

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

					meta.setContentType(manifest.getScenarioType());

					MMXRootObject implementation = null;

					for (final String smu : manifest.getModelURIs()) {
						URI rel = URI.createURI(smu);
						if (rel.isRelative()) {
							rel = rel.resolve(manifestURI);
						}
						if (preLoad) {
							if (implementation == null) {
								implementation = MMXCoreFactory.eINSTANCE.createMMXRootObject();
								result.setInstance(implementation);
							}

							final Resource r = resourceSet.createResource(rel);
							r.load(null);
							implementation.addSubModel((UUIDObject) r.getContents().get(0));

						}
						result.getSubModelURIs().add(rel.toString());
					}

					result.getDependencyUUIDs().addAll(manifest.getDependencyUUIDs());

					if (preLoad) {
						MMXCoreHandlerUtil.restoreProxiesForResources(resourceSet.getResources());
					}
					
					
					return result;
				}
			}
		} catch (final IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
}
