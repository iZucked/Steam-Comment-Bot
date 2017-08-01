/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.manifest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteStreams;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.util.CheckedBiConsumer;
import com.mmxlabs.common.util.CheckedBiFunction;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.IScenarioMigrationService;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.manager.InstanceData;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.util.ScenarioServiceUtils;
import com.mmxlabs.scenario.service.util.MMXAdaptersAwareCommandStack;
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
			synchronized (tempDirectories) {
				tempDirectories.add(uuidDir);
			}
		}
		return new File(uuidDir, escape(name) + ".lingo");
	}

	private String escape(final String name) {
		return name.replaceAll("[\\W&&[^ ]]", "-");
	}

	public static String storeToTemporaryFile(final ScenarioInstance instance) throws IOException {
		final File tempFile = INSTANCE.getTemporaryFile(instance);
		synchronized (tempFiles) {
			tempFiles.add(tempFile);
		}
		storeToFile(instance, tempFile);

		return tempFile.getAbsolutePath();
	}

	public static void storeToFile(final ScenarioInstance instance, final File file) throws IOException {

		ServiceHelper.withCheckedOptionalServiceConsumer(IScenarioCipherProvider.class, scenarioCipherProvider -> {
			storeToFile(instance, file, scenarioCipherProvider);
		});
	}

	public static void storeToFile(final ScenarioInstance instance, final File file, final IScenarioCipherProvider scenarioCipherProvider) throws IOException {
		final ResourceSet resourceSet = ResourceHelper.createResourceSet(scenarioCipherProvider);

		final IScenarioService scenarioService = SSDataManager.Instance.findScenarioService(instance);

		final URI rootObjectURI = URI.createURI("archive:" + URI.createFileURI(file.getAbsolutePath()) + "!/rootObject.xmi");

		@NonNull
		ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);
		@Nullable
		ModelReference modelReference = modelRecord.aquireReferenceIfLoaded("ScenarioStoragetUtil:1");
		// Already loaded?
		if (modelReference != null) {
			try {
				// try (final ModelReference modelReference = instance.getReference()) {
				final EObject rootObject = EcoreUtil.copy(modelReference.getInstance());

				final Resource r = ResourceHelper.createResource(resourceSet, rootObjectURI);
				r.getContents().add(rootObject);

				ResourceHelper.saveResource(r);
			} finally {
				modelReference.close();
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

	public static ScenarioInstance loadInstanceFromResourceURL(final URL resourceURL) throws IOException {

		final URI uri = URI.createURI(FileLocator.toFileURL(resourceURL).toString().replaceAll(" ", "%20"));
		return loadInstanceFromURI(uri);
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

	public static String storeToTemporaryFile(final ScenarioInstance instance, final EObject instanceData) throws IOException {
		final File tempFile = INSTANCE.getTemporaryFile(instance);
		tempFiles.add(tempFile);
		storeToFile(instance, instanceData, tempFile);

		return tempFile.getAbsolutePath();
	}

	public static void storeToFile(final ScenarioInstance instance, final EObject instanceData, final File file) throws IOException {

		ServiceHelper.withCheckedOptionalServiceConsumer(IScenarioCipherProvider.class, scenarioCipherProvider -> {
			storeToFile(instance, instanceData, file, scenarioCipherProvider);
		});
	}

	public static void storeToFile(final ScenarioInstance instance, final EObject instanceData, final File file, final IScenarioCipherProvider scenarioCipherProvider) throws IOException {
		final ResourceSet resourceSet = ResourceHelper.createResourceSet(scenarioCipherProvider);

		final URI rootObjectURI = URI.createURI("archive:" + URI.createFileURI(file.getAbsolutePath()) + "!/rootObject.xmi");

		// Already loaded?
		final EObject rootObject = EcoreUtil.copy(instanceData);
		final Resource r = ResourceHelper.createResource(resourceSet, rootObjectURI);
		r.getContents().add(rootObject);
		ResourceHelper.saveResource(r);

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
	public static ScenarioInstance createInstanceFromFile(final String filePath, final IScenarioCipherProvider scenarioCipherProvider) {
		return loadInstanceFromURI(URI.createFileURI(filePath), scenarioCipherProvider);
	}

	public static ScenarioInstance createInstanceFromFile(final String filePath) {
		return ServiceHelper.withOptionalService(IScenarioCipherProvider.class, scenarioCipherProvider -> {
			return createInstanceFromFile(filePath, scenarioCipherProvider);
		});
	}

	/**
	 * @param filePath
	 * @param preLoad
	 *            If true, load the resources and hook up submodels. If false, just hook up the URIs. Use false if this will be used in the {@link IScenarioService} / ModelService context
	 * @return
	 */
	public static ScenarioInstance createInstanceFromURI(final URI scenarioURI) {
		return ServiceHelper.withOptionalService(IScenarioCipherProvider.class, scenarioCipherProvider -> {

			return loadInstanceFromURI(scenarioURI, scenarioCipherProvider);
		});
	}

	public static @NonNull Pair<ScenarioInstance, ModelReference> loadFromResourceURL(final URL resourceURL, @NonNull IProgressMonitor monitor) throws Exception {
		final ScenarioInstance instance = loadInstanceFromResourceURL(resourceURL);
		return load(instance, monitor);
	}

	public static @NonNull Pair<ScenarioInstance, ModelReference> load(final URI scenarioURI, @NonNull IProgressMonitor monitor) throws Exception {
		final ScenarioInstance instance = createInstanceFromURI(scenarioURI);
		return load(instance, monitor);
	}

	public static @NonNull Pair<@NonNull ScenarioInstance, @NonNull ModelReference> load(final String filePath, @NonNull IProgressMonitor monitor) throws Exception {
		final ScenarioInstance instance = createInstanceFromFile(filePath);
		return load(instance, monitor);
	}

	/**
	 * Load a scenario from the filesystem, migrating if required. The {@link ScenarioInstance} and an initial {@link ModelReference} is returned. Once unloaded, it is not expected that the scenario
	 * will be able to be reloaded.
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 * @throws CommitException
	 * @throws ConcurrentAccessException
	 */
	public static @NonNull Pair<@NonNull ScenarioInstance, @NonNull ModelReference> load(final ScenarioInstance original, @NonNull IProgressMonitor monitor) throws Exception {
		monitor.beginTask("Load Scenario", 13);
		try {
			return ServiceHelper.withOptionalService(IScenarioCipherProvider.class, scenarioCipherProvider -> {

				// Handle migration
				final List<File> tmpFiles = new ArrayList<File>();

				final ScenarioInstance newInstance = ServiceHelper.withCheckedService(IScenarioMigrationService.class, scenarioMigrationService -> {
					final ExtensibleURIConverterImpl uc = new ExtensibleURIConverterImpl();

					// Create a copy of the data to avoid modifying it unexpectedly. E.g. this could come from a scenario data file on filesystem which should be left unchanged.
					final ScenarioInstance cpy = EcoreUtil.copy(original);

					{
						final String subModelURI = original.getRootObjectURI();
						final File f = File.createTempFile("migration-ssu", ".xmi");
						tmpFiles.add(f);
						// Create a temp file and generate a URI to it to pass into migration code.
						final URI tmpURI = URI.createFileURI(f.getCanonicalPath());
						assert tmpURI != null;
						final URI sourceURI = URI.createURI(subModelURI);
						assert sourceURI != null;
						ScenarioServiceUtils.copyURIData(uc, sourceURI, tmpURI);
						cpy.setRootObjectURI(tmpURI.toString());
						synchronized (tempFiles) {
							tempFiles.add(f);
						}
					}

					// Perform the migration!
					if (scenarioMigrationService != null) {
						try {
							scenarioMigrationService.migrateScenario(null, cpy, new SubProgressMonitor(monitor, 10));
						} catch (final RuntimeException e) {
							throw e;
						} catch (final Exception e) {
							throw new RuntimeException("Error migrating scenario", e);
						}
					}
					return cpy;
				});
				monitor.worked(1);
				final String uuid = EcoreUtil.generateUUID();

				monitor.worked(1);
				@NonNull
				final ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(newInstance, (record, m) -> {
					// final ResourceSet resourceSet = ResourceHelper.createResourceSet(scenarioCipherProvider);
					// final String rooObjectURI = newInstance.getRootObjectURI();
					// // acquire sub models
					// log.debug("Loading rootObject from " + rooObjectURI);
					// final URI uri = URI.createURI(rooObjectURI);
					// Resource resource;
					try {
						// resource = ResourceHelper.loadResource(resourceSet, uri);
						//
						// final EObject rootObject = resource.getContents().get(0);
						// if (rootObject == null) {
						// throw new NullPointerException();
						// }

						final ResourceSet resourceSet = ResourceHelper.createResourceSet(scenarioCipherProvider);
						final String rooObjectURI = newInstance.getRootObjectURI();
						final URI uri = URI.createURI(rooObjectURI);
						final Resource resource = ResourceHelper.loadResource(resourceSet, uri);

						final EObject rootObject = resource.getContents().get(0);

						// final EObject rootObject = r.getContents().get(0);
						final Pair<CommandProviderAwareEditingDomain, MMXAdaptersAwareCommandStack> p = initEditingDomain(resourceSet, rootObject, newInstance);
						final InstanceData data = new InstanceData(record, rootObject, p.getFirst(), p.getSecond(), (d) -> {

							throw new UnsupportedOperationException();
						}, (d) -> {
							// transaction.close();
							// sessionProvider.close();

							// Clean up tmp files
							for (final File f : tmpFiles) {
								f.delete();
								synchronized (tempFiles) {
									tempFiles.remove(f);
								}
							}
							tmpFiles.clear();
						});
						p.getSecond().setInstanceData(data);

						return data;
					} catch (final Exception e) {
						record.setLoadFailure(e);
						return null;
					}
				});
				monitor.worked(1);
				@NonNull
				final ModelReference ref = modelRecord.aquireReference("ScenarioStoragetUtil:2");
				if (ref == null) {
					throw new RuntimeException(modelRecord.getLoadFailure());
				}
				return new Pair<>(newInstance, ref);
			});
		} finally {
			monitor.done();
		}

	}

	public static void withExternalScenarioFromFile(final String filePath, final CheckedBiConsumer<ScenarioInstance, ModelReference, Exception> consumer, @NonNull IProgressMonitor monitor)
			throws Exception {
		final Pair<ScenarioInstance, ModelReference> p = load(filePath, monitor);
		try {
			consumer.accept(p.getFirst(), p.getSecond());
		} finally {
			p.getSecond().close();
		}
	}

	public static <R> R withExternalScenarioFromFile(final String filePath, final CheckedBiFunction<ScenarioInstance, ModelReference, R, Exception> consumer, @NonNull IProgressMonitor monitor)
			throws Exception {
		final Pair<ScenarioInstance, ModelReference> p = load(filePath, monitor);
		try {
			return consumer.apply(p.getFirst(), p.getSecond());
		} finally {
			p.getSecond().close();
		}
	}

	public static void withExternalScenarioFromFile(final File file, final CheckedBiConsumer<ScenarioInstance, ModelReference, Exception> consumer, @NonNull IProgressMonitor monitor)
			throws Exception {
		final Pair<ScenarioInstance, ModelReference> p = load(file.getAbsolutePath(), monitor);
		try {
			consumer.accept(p.getFirst(), p.getSecond());
		} finally {
			p.getSecond().close();
			// Added to help clean up with unit tests
			System.gc();
		}
	}

	public static void withExternalScenarioFromResourceURL(final URL resourceURL, final CheckedBiConsumer<ScenarioInstance, ModelReference, Exception> consumer) throws Exception {
		withExternalScenarioFromFile(new File(new URL(FileLocator.toFileURL(resourceURL).toString().replaceAll(" ", "%20")).toURI()), consumer, new NullProgressMonitor());
	}

	public static void withExternalScenarioFromResourceURL(final URL resourceURL, final CheckedBiConsumer<ScenarioInstance, ModelReference, Exception> consumer, @NonNull IProgressMonitor monitor)
			throws Exception {
		withExternalScenarioFromFile(new File(new URL(FileLocator.toFileURL(resourceURL).toString().replaceAll(" ", "%20")).toURI()), consumer, monitor);
	}

	public static <U extends EObject> void withExternalScenarioModel(final URI uri, final CheckedBiConsumer<ScenarioInstance, U, Exception> consumer, @NonNull IProgressMonitor monitor)
			throws Exception {
		final @NonNull Pair<@NonNull ScenarioInstance, @NonNull ModelReference> p = load(uri, monitor);
		try {
			consumer.accept(p.getFirst(), (U) p.getSecond().getInstance());
		} finally {
			p.getSecond().close();
		}
	}

	public static Pair<CommandProviderAwareEditingDomain, MMXAdaptersAwareCommandStack> initEditingDomain(final ResourceSet resourceSet, final EObject rootObject, final ScenarioInstance instance) {

		final MMXAdaptersAwareCommandStack commandStack = new MMXAdaptersAwareCommandStack(instance);

		commandStack.addCommandStackListener(new CommandStackListener() {

			@Override
			public void commandStackChanged(final EventObject event) {
				// instance.setDirty(commandStack.isSaveNeeded());
			}
		});

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		// Create the editing domain with a special command stack.
		final MMXRootObject mmxRootObject = (MMXRootObject) rootObject;

		final CommandProviderAwareEditingDomain editingDomain = new CommandProviderAwareEditingDomain(adapterFactory, commandStack, mmxRootObject, resourceSet);

		commandStack.setEditingDomain(editingDomain);

		return new Pair<>(editingDomain, commandStack);

	}

	public static InstanceData loadLocal(final @NonNull ScenarioInstance instance) throws Exception {

		return ServiceHelper.withOptionalService(IScenarioCipherProvider.class, scenarioCipherProvider -> {
			final ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);

			final List<File> tmpFiles = new ArrayList<File>();

			final ResourceSet resourceSet = ResourceHelper.createResourceSet(scenarioCipherProvider);
			final String rooObjectURI = instance.getRootObjectURI();
			final URI uri = URI.createURI(rooObjectURI);
			final Resource resource = ResourceHelper.loadResource(resourceSet, uri);

			final EObject rootObject = resource.getContents().get(0);

			// final EObject rootObject = r.getContents().get(0);
			final Pair<CommandProviderAwareEditingDomain, MMXAdaptersAwareCommandStack> p = initEditingDomain(resourceSet, rootObject, instance);
			final InstanceData data = new InstanceData(modelRecord, rootObject, p.getFirst(), p.getSecond(), (d) -> {

				throw new UnsupportedOperationException();
			}, (d) -> {
				// Clean up tmp files
				for (final File f : tmpFiles) {
					f.delete();
				}
				tmpFiles.clear();
			});
			p.getSecond().setInstanceData(data);

			return data;
		});

	}
}
