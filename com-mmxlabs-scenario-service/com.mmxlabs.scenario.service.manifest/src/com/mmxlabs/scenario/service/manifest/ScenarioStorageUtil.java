/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.manifest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteStreams;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.util.ResourceHelper;
import com.mmxlabs.scenario.service.util.encryption.IScenarioCipherProvider;

/**
 * Utility class for getting a scenario out into a zipfile or vice-versa
 * 
 * @author hinton
 * 
 */
public class ScenarioStorageUtil {

	private static final Logger log = LoggerFactory.getLogger(ScenarioStorageUtil.class);

	static final ScenarioStorageUtil INSTANCE = new ScenarioStorageUtil();
	protected Path storageDirectory;
	protected File lastTemporaryFile;

	private static final List<File> tempFiles = new LinkedList<>();
	private static final List<File> tempDirectories = new LinkedList<>();

	protected ScenarioStorageUtil() {
		try {
			storageDirectory = Files.createTempDirectory("ScenarioStorage");
			// there is a race here; the only way to really avoid it is to use
			// something like java.nio in java 7, which has a createTempDir method.
			// if (storageDirectory.delete()) {
			// storageDirectory.mkdir();
			// }
			// TODO should we delete this on finalize? if the user has copied something she would expect
			// it to remain copied so perhaps we should delete all but the most recent copied thing?
			// Is that a job for the copy handler anyway?

			Runtime.getRuntime().addShutdownHook(new Thread() {

				@Override
				public void run() {
					/* Delete your file here. */
					while (!tempFiles.isEmpty()) {
						final File f = tempFiles.remove(0);
						try {
							Files.deleteIfExists(f.toPath());
						} catch (final IOException e) {
							log.error(e.getMessage(), e);
						}
					}
					while (!tempDirectories.isEmpty()) {
						final File f = tempDirectories.remove(0);
						if (f.exists()) {
							f.delete();
						}
					}
					try {
						Files.deleteIfExists(storageDirectory);
					} catch (final IOException e) {
						log.error(e.getMessage(), e);
					}
				}
			});

		} catch (final IOException e) {
			storageDirectory = null;
		}

	}

	protected File getTemporaryFile(final ScenarioInstance instance) {
		final String name = instance.getName();
		final String uuid = instance.getUuid();

		// We create a temporary folder so that the real scenario filename can be used, otherwise Windows paste handler will take the random filename.
		final File uuidDir = new File(storageDirectory.toFile(), escape(uuid) + ".d");
		if (uuidDir.exists() == false) {
			uuidDir.mkdirs();
			tempDirectories.add(uuidDir);
		}
		return new File(uuidDir, escape(name) + ".lingo");
	}

	private String escape(final String name) {
		return name.replaceAll("[\\W&&[^ ]]", "-");
	}

	public static String storeToTemporaryFile(final ScenarioInstance instance) throws IOException {
		final File tempFile = INSTANCE.getTemporaryFile(instance);
		tempFiles.add(tempFile);
		storeToFile(instance, tempFile);

		return tempFile.getAbsolutePath();
	}

	public static void storeToFile(final ScenarioInstance instance, final File file) throws IOException {

		ServiceHelper.withCheckedOptionalService(IScenarioCipherProvider.class, scenarioCipherProvider -> {
			storeToFile(instance, file, scenarioCipherProvider);
		});
	}

	public static void storeToFile(final ScenarioInstance instance, final File file, final IScenarioCipherProvider scenarioCipherProvider) throws IOException {
		final ResourceSet resourceSet = ResourceHelper.createResourceSet(scenarioCipherProvider);

		final IScenarioService scenarioService = instance.getScenarioService();

		final URI rootObjectURI = URI.createURI("archive:" + URI.createFileURI(file.getAbsolutePath()) + "!/rootObject.xmi");

		// Already loaded?
		if (instance.getInstance() != null) {
			try (final ModelReference modelReference = instance.getReference()) {
				final EObject rootObject = EcoreUtil.copy(modelReference.getInstance());

				final Resource r = ResourceHelper.createResource(resourceSet, rootObjectURI);
				r.getContents().add(rootObject);

				ResourceHelper.saveResource(r);
			}
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
			manifest.setClientScenarioVersion(instance.getClientScenarioVersion());
			manifest.setClientVersionContext(instance.getClientVersionContext());
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
	public static ScenarioInstance loadInstanceFromFile(final String filePath, final IScenarioCipherProvider scenarioCipherProvider) {
		return loadInstanceFromURI(URI.createFileURI(filePath), scenarioCipherProvider);
	}

	public static ScenarioInstance loadInstanceFromFile(final String filePath) {
		return ServiceHelper.withOptionalService(IScenarioCipherProvider.class, scenarioCipherProvider -> {
			return loadInstanceFromFile(filePath, scenarioCipherProvider);
		});
	}

	/**
	 * @param filePath
	 * @param preLoad
	 *            If true, load the resources and hook up submodels. If false, just hook up the URIs. Use false if this will be used in the {@link IScenarioService} / ModelService context
	 * @return
	 */
	public static ScenarioInstance loadInstanceFromURI(final URI scenarioURI) {
		return ServiceHelper.withOptionalService(IScenarioCipherProvider.class, scenarioCipherProvider -> {

			return loadInstanceFromURI(scenarioURI, scenarioCipherProvider);
		});
	}

	public static ScenarioInstance loadInstanceFromURI(final URI scenarioURI, final IScenarioCipherProvider scenarioCipherProvider) {
		final URI manifestURI = URI.createURI("archive:" + scenarioURI.toString() + "!/MANIFEST.xmi");
		final ResourceSet resourceSet = ResourceHelper.createResourceSet(scenarioCipherProvider);

		try {

			final Resource resource = ResourceHelper.loadResource(resourceSet, manifestURI);
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

					result.setClientVersionContext(manifest.getClientVersionContext());
					result.setClientScenarioVersion(manifest.getClientScenarioVersion());

					meta.setContentType(manifest.getScenarioType());

					for (final String smu : manifest.getModelURIs()) {
						URI rel = URI.createURI(smu);
						if (rel.isRelative()) {
							rel = rel.resolve(manifestURI);
						}
						result.setRootObjectURI(rel.toString());
						break;
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
