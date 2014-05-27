/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.manifest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.Nullable;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteStreams;
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
		return new File(uuidDir, escape(name) + ".lingo");
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
		final ResourceSet resourceSet = ResourceHelper.createResourceSet(getScenarioCipherProvider());

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
	public static ScenarioInstance loadInstanceFromFile(final String filePath, IScenarioCipherProvider scenarioCipherProvider) {
		return loadInstanceFromURI(URI.createFileURI(filePath), scenarioCipherProvider);
	}

	/**
	 * @param filePath
	 * @param preLoad
	 *            If true, load the resources and hook up submodels. If false, just hook up the URIs. Use false if this will be used in the {@link IScenarioService} / ModelService context
	 * @return
	 */
	public static ScenarioInstance loadInstanceFromURI(final URI scenarioURI, IScenarioCipherProvider scenarioCipherProvider) {
		final URI manifestURI = URI.createURI("archive:" + scenarioURI.toString() + "!/MANIFEST.xmi");
		final ResourceSet resourceSet = ResourceHelper.createResourceSet(scenarioCipherProvider);

		try {

			Resource resource = ResourceHelper.loadResource(resourceSet, manifestURI);
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

	@Nullable
	private static IScenarioCipherProvider getScenarioCipherProvider() {
		final BundleContext bundleContext = FrameworkUtil.getBundle(ScenarioStorageUtil.class).getBundleContext();
		final ServiceReference<IScenarioCipherProvider> serviceReference = bundleContext.getServiceReference(IScenarioCipherProvider.class);
		if (serviceReference != null) {
			return bundleContext.getService(serviceReference);
		}
		return null;
	}

}
