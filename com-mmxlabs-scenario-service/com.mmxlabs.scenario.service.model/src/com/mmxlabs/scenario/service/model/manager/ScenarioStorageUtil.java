/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.manager;

import java.io.CharConversionException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.io.FileDeleter;
import com.mmxlabs.common.util.CheckedBiConsumer;
import com.mmxlabs.common.util.CheckedBiFunction;
import com.mmxlabs.common.util.TriConsumer;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.manifest.ModelArtifact;
import com.mmxlabs.scenario.service.manifest.StorageType;
import com.mmxlabs.scenario.service.model.util.MMXAdaptersAwareCommandStack;
import com.mmxlabs.scenario.service.model.util.ResourceHelper;
import com.mmxlabs.scenario.service.model.util.ScenarioServiceUtils;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;

/**
 * Utility class for getting a scenario out into a zipfile or vice-versa
 * 
 * @author hinton
 * 
 */
public class ScenarioStorageUtil {

	public static class EncryptedScenarioException extends RuntimeException {

		public EncryptedScenarioException(Exception e) {
			super(e);
		}

	}

	public static final @NonNull String DEFAULT_CONTENT_TYPE = "com.mmxlabs.shiplingo.platform.models.manifest.scnfile";
	public static final @NonNull String PATH_ROOT_OBJECT = "rootObject.xmi";
	public static final @NonNull String PATH_MANIFEST_OBJECT = "MANIFEST.xmi";

	private static final Logger log = LoggerFactory.getLogger(ScenarioStorageUtil.class);

	static final ScenarioStorageUtil INSTANCE = new ScenarioStorageUtil();
	protected Path storageDirectory;
	protected File lastTemporaryFile;

	protected ScenarioStorageUtil() {
		try {
			final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();

			storageDirectory = workspaceLocation.append("temp").toFile().toPath();
			if (!storageDirectory.toFile().exists()) {
				if (!storageDirectory.toFile().mkdirs()) {
					log.error("Unable to create temporary directory: " + storageDirectory.toString());
				}
			}
			// there is a race here; the only way to really avoid it is to use
			// something like java.nio in java 7, which has a createTempDir method.
			// if (storageDirectory.delete()) {
			// storageDirectory.mkdir();
			// }
			// TODO should we delete this on finalize? if the user has copied something she would expect
			// it to remain copied so perhaps we should delete all but the most recent copied thing?
			// Is that a job for the copy handler anyway?

			final boolean secureDelete = LicenseFeatures.isPermitted(FileDeleter.LICENSE_FEATURE__SECURE_DELETE);

			// Define a function here to try and avoid class not found exceptions later on when running the shutdown hook.
			// Hopefully this means the class is still referenced after the common util plugin has stopped.
			final CheckedBiFunction<File, Boolean, Boolean, IOException> d = FileDeleter::delete;

			Runtime.getRuntime().addShutdownHook(new Thread("Cleanup hook") {

				@Override
				public void run() {
					if (storageDirectory != null) {
						final File tempDirectory = storageDirectory.toFile();
						if (tempDirectory.exists() && tempDirectory.isDirectory()) {
							try {
								final Path tempDir = tempDirectory.toPath();
								Files.walkFileTree(tempDir, new SimpleFileVisitor<Path>() {
									@Override
									public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
										if (!tempDir.equals(dir)) {
											dir.toFile().delete();
										}
										return FileVisitResult.CONTINUE;
									}

									@Override
									public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
										d.apply(file.toFile(), secureDelete);
										return FileVisitResult.CONTINUE;
									}
								});
							} catch (final IOException e) {
								e.printStackTrace();
							}
						}
					}

					try {
						Files.deleteIfExists(storageDirectory);
					} catch (final IOException e) {
						log.error(e.getMessage(), e);
					}
				}
			});

		} catch (final Exception e) {
			storageDirectory = null;
		}

	}

	protected File getTemporaryFile(final ScenarioModelRecord modelRecord) throws IOException {
		final String name = modelRecord.getName();
		final Path tempDirectory = Files.createTempDirectory(getTempDirectory().toPath(), "lingo");

		return new File(tempDirectory.toFile(), escape(name) + ".lingo");
	}

	private String escape(final String name) {
		return name.replaceAll("[\\W&&[^ ]]", "-");
	}

	@NonNullByDefault
	public static URI createArtifactURI(final URI archiveURI, final String fragment) {
		return URI.createURI("archive:" + archiveURI.toString() + "!/" + fragment);
	}

	public static File storeToTemporaryFile(final ScenarioModelRecord instance) throws IOException {
		final File tempFile = INSTANCE.getTemporaryFile(instance);
		storeToFile(instance, tempFile);

		return tempFile;
	}

	public static void storeToFile(final ScenarioModelRecord modelRecord, final File file) throws IOException {

		ServiceHelper.withCheckedOptionalServiceConsumer(IScenarioCipherProvider.class, scenarioCipherProvider -> {
			storeToFile(modelRecord, file, scenarioCipherProvider);
		});
	}

	public static void storeToFile(final @NonNull ScenarioModelRecord modelRecord, final @NonNull File file, final @Nullable IScenarioCipherProvider scenarioCipherProvider) throws IOException {
		try (final ModelReference modelReference = modelRecord.aquireReference("ScenarioStoragetUtil:storeToFile:1")) {
			if (modelReference != null) {

				final Map<String, EObject> extraDataObjects = createCopyOfExtraData(modelRecord);

				storeToURI(EcoreUtil.generateUUID(), EcoreUtil.copy(modelReference.getInstance()), extraDataObjects, modelRecord.getManifest(), URI.createFileURI(file.getAbsolutePath()),
						scenarioCipherProvider);
			}
		}
	}

	public static void storeCopyToFile(final @NonNull IScenarioDataProvider scenarioDataProvider, final @NonNull File file) throws IOException {

		final ScenarioModelRecord tmpRecord = ScenarioStorageUtil.createFromCopyOf(file.getName(), scenarioDataProvider);
		storeToFile(tmpRecord, file);
	}

	public static Map<String, EObject> createCopyOfExtraData(final ScenarioModelRecord modelRecord) {
		final Map<String, EObject> extraDataObjects = new HashMap<>();
		// TODO: This data may change after loading/migration
		for (final ModelArtifact artifact : modelRecord.getManifest().getModelDependencies()) {
			if (artifact.getStorageType() == StorageType.COLOCATED) {
				@NonNull
				final ModelRecord extraDataRecord = modelRecord.getExtraDataRecord(ISharedDataModelType.registry().lookup(artifact.getKey()));
				@NonNull
				final ModelReference extraRef = extraDataRecord.aquireReference("ScenarioStoragetUtil:createCopyOfExtraData:a");

				extraDataObjects.put(artifact.getKey(), EcoreUtil.copy(extraRef.getInstance()));
			}
		}
		return extraDataObjects;
	}

	public static Map<String, EObject> createCopyOfExtraData(final IScenarioDataProvider scenarioDataProvider) {
		final Map<String, EObject> extraDataObjects = new HashMap<>();
		// TODO: This data may change after loading/migration
		for (final ModelArtifact artifact : scenarioDataProvider.getManifest().getModelDependencies()) {
			if (artifact.getStorageType() == StorageType.COLOCATED && artifact.getType().equals("EOBJECT")) {
				final ISharedDataModelType key = ISharedDataModelType.REGISTRY.lookup(artifact.getKey());
				final EObject data = (EObject) scenarioDataProvider.getExtraData(key);
				extraDataObjects.put(artifact.getKey(), EcoreUtil.copy(data));
			}
		}
		return extraDataObjects;
	}

	public static void storeToURI(final String uuid, final @NonNull EObject rootObject, final Map<String, EObject> extraDataObjects, final Manifest manifest, final @NonNull URI archiveURI,
			final @Nullable IScenarioCipherProvider scenarioCipherProvider) throws IOException {
		final ResourceSet resourceSet = ResourceHelper.createResourceSet(scenarioCipherProvider);

		final URI rootObjectURI = createArtifactURI(archiveURI, PATH_ROOT_OBJECT);

		final Resource r = ResourceHelper.createResource(resourceSet, rootObjectURI);
		assert rootObject.eResource() == null;

		r.getContents().add(rootObject);

		ResourceHelper.saveResource(r);

		// TODO: This data may change after loading/migration
		for (final ModelArtifact artifact : manifest.getModelDependencies()) {
			if (artifact.getStorageType() == StorageType.COLOCATED) {

				final URI dURI = createArtifactURI(archiveURI, artifact.getPath());
				final Resource r2 = ResourceHelper.createResource(resourceSet, dURI);
				r2.getContents().add(extraDataObjects.get(artifact.getKey()));
				ResourceHelper.saveResource(r2);
			}
		}

		// Now store the metadata
		{
			final URI manifestURI = createArtifactURI(archiveURI, PATH_MANIFEST_OBJECT);
			final Resource manifestResource = resourceSet.createResource(manifestURI);

			manifestResource.getContents().add(EcoreUtil.copy(manifest));

			manifestResource.save(null);
		}

	}

	public static void storeToURI(final String uuid, final @NonNull URI sourceURI, final Map<String, URI> extraDataURIs, final Manifest manifest, final @NonNull URI archiveURI,
			final @Nullable IScenarioCipherProvider scenarioCipherProvider) throws IOException {
		final ResourceSet resourceSet = ResourceHelper.createResourceSet(scenarioCipherProvider);

		final URI rootObjectURI = createArtifactURI(archiveURI, PATH_ROOT_OBJECT);
		ScenarioServiceUtils.copyURIData(new ExtensibleURIConverterImpl(), sourceURI, rootObjectURI);

		// TODO: This data may change after loading/migration
		for (final ModelArtifact artifact : manifest.getModelDependencies()) {
			if (artifact.getStorageType() == StorageType.COLOCATED) {
				final URI sURI = extraDataURIs.get(artifact.getKey());
				final URI dURI = createArtifactURI(archiveURI, artifact.getPath());
				ScenarioServiceUtils.copyURIData(new ExtensibleURIConverterImpl(), sURI, dURI);
			}
		}

		// Now store the metadata
		{
			final Manifest copy_manifest = EcoreUtil.copy(manifest);
			copy_manifest.setUUID(uuid);

			final URI manifestURI = createArtifactURI(archiveURI, PATH_MANIFEST_OBJECT);
			final Resource manifestResource = resourceSet.createResource(manifestURI);

			manifestResource.getContents().add(copy_manifest);

			manifestResource.save(null);
		}

	}

	public static ScenarioModelRecord loadInstanceFromURI(final URI archiveURI, final boolean copyToTemp, final boolean allowSave, final @Nullable IScenarioCipherProvider scenarioCipherProvider) {
		return loadInstanceFromURI(archiveURI, copyToTemp, allowSave, true, scenarioCipherProvider);
	}

	public static ScenarioModelRecord loadInstanceFromURI(final URI archiveURI, final boolean copyToTemp, final boolean allowSave, final boolean allowMigration,
			final @Nullable IScenarioCipherProvider scenarioCipherProvider) {
		try {
			return loadInstanceFromURIChecked(archiveURI, copyToTemp, allowSave, allowMigration, scenarioCipherProvider);
		} catch (final Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public static ScenarioModelRecord loadInstanceFromURIChecked(final URI archiveURI, final boolean copyToTemp, final boolean allowSave, final boolean allowMigration,
			final @Nullable IScenarioCipherProvider scenarioCipherProvider) throws Exception {

		final URI manifestURI = createArtifactURI(archiveURI, PATH_MANIFEST_OBJECT);
		final ResourceSet resourceSet = ResourceHelper.createResourceSet(scenarioCipherProvider);

		try {
			final Resource manifestResource = ResourceHelper.loadResource(resourceSet, manifestURI);
			if (manifestResource.getContents().size() == 1) {
				final EObject top = manifestResource.getContents().get(0);
				if (top instanceof Manifest) {
					final Manifest manifest = (Manifest) top;

					final String name = archiveURI.trimFileExtension().lastSegment();
					final String uuid = manifest.getUUID();

					// how to get dependent data into the model record? (load function will trigger migration.... and changes to manifest, and version data,, -- are these reflected here?
					// version data is updated
					final LinkedList<File> tmpFilesList = new LinkedList<>();

					final ScenarioModelRecord modelRecord;
					{
						final BiConsumer<ModelRecord, InstanceData> saveCallback = allowSave ? createSaveCallback(archiveURI, resourceSet, PATH_ROOT_OBJECT) : (mr, data) -> {
							// Save not permitted
						};
						final Consumer<InstanceData> closeCallback = createCloseCallback(tmpFilesList);

						final TriConsumer<ModelRecord, URI, IProgressMonitor> migrationCallback = (r, uri, mon) -> {
							if (r instanceof ScenarioModelRecord) {
								final ScenarioModelRecord mr = (ScenarioModelRecord) r;
								migrateLiNGOFile(mr, mon, uri, allowMigration);
								// Add any new artifacts
								for (final ModelArtifact artifact : manifest.getModelDependencies()) {
									final ISharedDataModelType<?> artifactKey = ISharedDataModelType.registry().lookup(artifact.getKey());
									if (!mr.hasExtraDataRecord(artifactKey)) {
										final LinkedList<File> extraDataTmp = new LinkedList<>();

										final BiConsumer<ModelRecord, InstanceData> l_saveCallback = allowSave ? createSaveCallback(uri, resourceSet, artifact.getPath()) : (l_mr, l_data) -> {
											// Save not permitted
										};
										final Consumer<InstanceData> l_closeCallback = createCloseCallback(extraDataTmp);

										final ModelRecord l_r = new ModelRecord(loadFromReadWriteURI(uri, manifest, resourceSet, artifact.getPath(), copyToTemp, allowSave, extraDataTmp,
												scenarioCipherProvider, l_saveCallback, l_closeCallback, null));

										mr.addExtraDataRecord(artifactKey, l_r);
									}
								}
							}
						};

						modelRecord = new ScenarioModelRecord(manifest, loadFromReadWriteURI(archiveURI, manifest, resourceSet, PATH_ROOT_OBJECT, copyToTemp, allowSave, tmpFilesList,
								scenarioCipherProvider, saveCallback, closeCallback, migrationCallback));
						modelRecord.setName(name);
					}

					// TODO: This data may change after loading/migration
					for (final ModelArtifact artifact : manifest.getModelDependencies()) {
						final LinkedList<File> extraDataTmp = new LinkedList<>();

						final BiConsumer<ModelRecord, InstanceData> saveCallback = allowSave ? createSaveCallback(archiveURI, resourceSet, artifact.getPath()) : (mr, data) -> {
							// Save not permitted
						};
						final Consumer<InstanceData> closeCallback = createCloseCallback(extraDataTmp);

						final ModelRecord r = new ModelRecord(loadFromReadWriteURI(archiveURI, manifest, resourceSet, artifact.getPath(), copyToTemp, allowSave, extraDataTmp, scenarioCipherProvider,
								saveCallback, closeCallback, null));

						modelRecord.addExtraDataRecord(ISharedDataModelType.registry().lookup(artifact.getKey()), r);
					}
					return modelRecord;
				}
			}
		} catch (CharConversionException e) {
			throw new EncryptedScenarioException(e);
		}
		return null;
	}

	public static @NonNull Pair<ScenarioModelRecord, IScenarioDataProvider> loadFromResourceURL(final URL resourceURL) {
		return ServiceHelper.withCheckedOptionalService(IScenarioCipherProvider.class, scenarioCipherProvider -> {
			final ScenarioModelRecord modelRecord = loadInstanceFromURI(URI.createURI(resourceURL.toString()), true, false, scenarioCipherProvider);
			final IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ScenarioStorageUtil:loadFromResourceURL");
			return new Pair<>(modelRecord, scenarioDataProvider);
		});
	}

	public static void withExternalScenarioFromResourceURLConsumer(final URL resourceURL, final CheckedBiConsumer<@NonNull ScenarioModelRecord, @NonNull IScenarioDataProvider, Exception> consumer,
			final IProgressMonitor monitor) throws Exception {
		ServiceHelper.withCheckedOptionalServiceConsumer(IScenarioCipherProvider.class, scenarioCipherProvider -> {
			final ScenarioModelRecord modelRecord = loadInstanceFromURI(URI.createURI(resourceURL.toString()), true, false, scenarioCipherProvider);
			try (IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ScenarioStorageUtil:withExternalScenarioFromResourceURL", monitor)) {
				consumer.accept(modelRecord, scenarioDataProvider);
			}
		});
	}

	public static void withExternalScenarioFromResourceURLConsumer(final URL resourceURL, final CheckedBiConsumer<@NonNull ScenarioModelRecord, @NonNull IScenarioDataProvider, Exception> consumer)
			throws Exception {
		ServiceHelper.withCheckedOptionalServiceConsumer(IScenarioCipherProvider.class, scenarioCipherProvider -> {
			final ScenarioModelRecord modelRecord = loadInstanceFromURI(URI.createURI(resourceURL.toString()), true, false, scenarioCipherProvider);
			try (IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ScenarioStorageUtil:withExternalScenarioFromResourceURL")) {
				consumer.accept(modelRecord, scenarioDataProvider);
			}
		});
	}

	public static <T> T withExternalScenarioFromResourceURL(final URL resourceURL, final CheckedBiFunction<@NonNull ScenarioModelRecord, @NonNull IScenarioDataProvider, T, Exception> consumer)
			throws Exception {
		return ServiceHelper.withCheckedOptionalService(IScenarioCipherProvider.class, scenarioCipherProvider -> {
			final ScenarioModelRecord modelRecord = loadInstanceFromURI(URI.createURI(resourceURL.toString()), true, false, scenarioCipherProvider);
			try (IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ScenarioStorageUtil:withExternalScenarioFromResourceURL")) {
				return consumer.apply(modelRecord, scenarioDataProvider);
			}
		});
	}

	public static Pair<@NonNull CommandProviderAwareEditingDomain, @NonNull MMXAdaptersAwareCommandStack> initEditingDomain(final Manifest manifest, final ResourceSet resourceSet,
			final EObject rootObject) {

		final MMXAdaptersAwareCommandStack commandStack = new MMXAdaptersAwareCommandStack();

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

		commandStack.setEditingDomain(manifest, editingDomain);

		return new Pair<>(editingDomain, commandStack);
	}

	private static BiFunction<ModelRecord, IProgressMonitor, InstanceData> loadFromReadWriteURI(final URI originalArchiveURI, final Manifest manifest, final ResourceSet resourceSet, final String path,
			final boolean copyToTemp, final boolean allowSave, final List<File> tempFiles, @Nullable final IScenarioCipherProvider scenarioCipherProvider,
			final BiConsumer<ModelRecord, InstanceData> saveCallback, final Consumer<InstanceData> closeCallback, final TriConsumer<ModelRecord, URI, IProgressMonitor> migrationCallback) {
		return (modelRecord, monitor) -> {
			try {
				monitor.beginTask("Loading scenario", 100);

				if (modelRecord.isLoaded()) {
					throw new IllegalStateException();
				}
				URI archiveURI;
				if (copyToTemp) {
					final ExtensibleURIConverterImpl uc = new ExtensibleURIConverterImpl();

					final File f = File.createTempFile("temp", ".lingo", ScenarioStorageUtil.getTempDirectory());
					tempFiles.add(f);
					// Create a temp file and generate a URI to it to pass into migration code.
					final URI tmpURI = URI.createFileURI(f.getCanonicalPath());
					assert tmpURI != null;
					ScenarioServiceUtils.copyURIData(uc, originalArchiveURI, tmpURI);

					archiveURI = tmpURI;

					synchronized (tempFiles) {
						tempFiles.add(f);
					}
				} else {
					archiveURI = originalArchiveURI;
				}

				// log.debug("Instance " + scenarioInstance.getName() + " (" + scenarioInstance.getUuid() + ") needs loading");

				if (migrationCallback != null) {
					migrationCallback.accept(modelRecord, archiveURI, monitor);
				}
				monitor.setTaskName("Loading scenario");

				// log.debug("Instance " + scenarioInstance.getName() + " (" + scenarioInstance.getUuid() + ") needs loading");

				// acquire sub models
				log.debug("Loading rootObject from " + archiveURI);

				final URI rootObjectURI = createArtifactURI(archiveURI, path);
				final Resource resource = ResourceHelper.loadResource(resourceSet, rootObjectURI);

				final EObject implementation = resource.getContents().get(0);

				if (implementation == null) {
					throw new IOException("Null value for model instance " + rootObjectURI);
				}

				final Pair<@NonNull CommandProviderAwareEditingDomain, @NonNull MMXAdaptersAwareCommandStack> p = initEditingDomain(manifest, resourceSet, implementation);
				final EditingDomain domain = p.getFirst();

				final InstanceData data = new InstanceData(modelRecord, implementation, domain, p.getSecond(), saveCallback, (d) -> {
					if (closeCallback != null) {
						closeCallback.accept(d);
					}
					// Close callback - Nothing to do
					resource.unload();
				});
				p.getSecond().setInstanceData(data);

				return data;
			} catch (final Exception e) {
				modelRecord.setLoadFailure(e);
				return null;
			} finally {
				monitor.done();
			}
		};
	}

	public static void migrateLiNGOFile(final ScenarioModelRecord modelRecord, final IProgressMonitor monitor, final URI archiveURI, final boolean allowMigration) {
		ServiceHelper.withOptionalServiceConsumer(IScenarioMigrationService.class, scenarioMigrationService -> {
			if (scenarioMigrationService != null) {
				if (scenarioMigrationService.needsMigrating(archiveURI, modelRecord.getManifest())) {
					if (!allowMigration) {
						throw new MigrationForbiddenException();
					}
					try {
						scenarioMigrationService.migrateScenario(archiveURI, modelRecord.getManifest(), SubMonitor.convert(monitor, "Migrate scenario", 100));
					} catch (final RuntimeException e) {
						throw e;
					} catch (final Exception e) {
						throw new RuntimeException("Error migrating scenario", e);
					}
				}
			}
		});
	}

	private static Consumer<InstanceData> createCloseCallback(final List<File> tmpFiles) {
		return d -> {
			final boolean secureDelete = LicenseFeatures.isPermitted(FileDeleter.LICENSE_FEATURE__SECURE_DELETE);
			for (final File f : tmpFiles) {
				try {
					FileDeleter.delete(f, secureDelete);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
			tmpFiles.clear();
		};
	}

	private static BiConsumer<ModelRecord, InstanceData> createSaveCallback(final URI archiveURI, final ResourceSet resourceSet, final String fragment) {
		final BiConsumer<ModelRecord, InstanceData> saveCallback = (modelRecord, d) -> {

			{
				final URI backupTarget = archiveURI.appendFileExtension("backup");
				// Copy scenario before saving
				try {
					ScenarioServiceUtils.copyURIData(new ExtensibleURIConverterImpl(), archiveURI, backupTarget);
				} catch (final IOException e1) {
					e1.printStackTrace();
				}
			}

			try (ModelReference ref = modelRecord.aquireReference("ScenarioStorageUtil:2")) {
				try {
					final Resource resource = resourceSet.getResource(createArtifactURI(archiveURI, fragment), false);
					ResourceHelper.saveResource(resource);
				} catch (final Exception e) {
					e.printStackTrace();
				}

				// This is not very nice
				if (modelRecord instanceof ScenarioModelRecord) {
					final ScenarioModelRecord scenarioModelRecord = (ScenarioModelRecord) modelRecord;
					// Save manifest, migration may have changed it.
					{
						final URI manifestURI = createArtifactURI(archiveURI, PATH_MANIFEST_OBJECT);
						final Manifest manifest = scenarioModelRecord.getManifest();

						final Resource manifestResource = resourceSet.getResource(manifestURI, true);
						manifestResource.getContents().clear();
						// Copy before save as it will be reparented otherwise.
						// FIXME: Keep hold of manifest resource(set)?
						manifestResource.getContents().add(EcoreUtil.copy(manifest));
						// manifest.getModelURIs().add(PATH_MANIFEST_OBJECT);
						try {
							manifestResource.save(null);
						} catch (final IOException e) {
							e.printStackTrace();
						}
					}
				}

				final BasicCommandStack commandStack = (BasicCommandStack) d.getCommandStack();
				commandStack.saveIsDone();
			}
		};
		return saveCallback;
	}

	public static ResourceSet createResourceSet(@Nullable final IScenarioCipherProvider cipherProvider) {
		return ResourceHelper.createResourceSet(cipherProvider);
	}

	private static BiFunction<ModelRecord, IProgressMonitor, InstanceData> loadFromInstance(final Manifest manifest, final EObject rootObject, final String path) {
		return (record, monitor) -> {
			try {

				// In-memory instance, no need for cipher
				final ResourceSet resourceSet = ResourceHelper.createResourceSet(null);

				final Resource r = resourceSet.createResource(URI.createURI(path));
				assert rootObject.eResource() == null;
				r.getContents().add(rootObject);

				final Pair<CommandProviderAwareEditingDomain, MMXAdaptersAwareCommandStack> p = initEditingDomain(manifest, resourceSet, rootObject);

				final InstanceData data = new InstanceData(record, rootObject, p.getFirst(), p.getSecond(), (mr, d) -> {
					// Save callback - no permitted
					throw new UnsupportedOperationException();
				}, (d) -> {
					// Close callback - Nothing to do
					r.unload();
				});
				p.getSecond().setInstanceData(data);

				return data;
			} catch (final Exception e) {
				record.setLoadFailure(e);
				return null;
			}
		};
	}

	public static @NonNull ScenarioModelRecord createFromCopyOf(final String name, final IScenarioDataProvider scenarioDataProvider) {
		final Manifest manifest = EcoreUtil.copy(scenarioDataProvider.getManifest());
		manifest.setUUID(EcoreUtil.generateUUID());

		final EObject rootObject = EcoreUtil.copy(scenarioDataProvider.getScenario());
		@NonNull
		final ScenarioModelRecord modelRecord = new ScenarioModelRecord(manifest, loadFromInstance(manifest, rootObject, PATH_ROOT_OBJECT));

		// TODO: This data may change after loading/migration
		for (final ModelArtifact artifact : manifest.getModelDependencies()) {
			if (artifact.getStorageType() == StorageType.COLOCATED) {
				final ISharedDataModelType<?> key = ISharedDataModelType.registry().lookup(artifact.getKey());
				final ModelRecord r = new ModelRecord(loadFromInstance(manifest, EcoreUtil.copy((EObject) scenarioDataProvider.getExtraData(key)), artifact.getPath()));
				modelRecord.addExtraDataRecord(key, r);
			}
		}

		modelRecord.setName(name);

		return modelRecord;

	}

	/**
	 * Note this will relocate EObjects into new resources.
	 * 
	 * @param name
	 * @param scenarioDataProvider
	 * @return
	 */
	public static @NonNull ScenarioModelRecord createFrom(final String name, final IScenarioDataProvider scenarioDataProvider) {
		final Manifest manifest = EcoreUtil.copy(scenarioDataProvider.getManifest());
		manifest.setUUID(EcoreUtil.generateUUID());

		final EObject rootObject = scenarioDataProvider.getScenario();
		@NonNull
		final ScenarioModelRecord modelRecord = new ScenarioModelRecord(manifest, loadFromInstance(manifest, rootObject, PATH_ROOT_OBJECT));

		// TODO: This data may change after loading/migration
		for (final ModelArtifact artifact : manifest.getModelDependencies()) {
			if (artifact.getStorageType() == StorageType.COLOCATED) {
				final ISharedDataModelType<?> key = ISharedDataModelType.registry().lookup(artifact.getKey());
				final ModelRecord r = new ModelRecord(loadFromInstance(manifest, (EObject) scenarioDataProvider.getExtraData(key), artifact.getPath()));
				modelRecord.addExtraDataRecord(key, r);
			}
		}

		modelRecord.setName(name);

		return modelRecord;

	}

	public static File getTempDirectory() {
		final File file = INSTANCE.storageDirectory.toFile();
		// Sometimes the temp folder is not present.
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	public static @Nullable Manifest loadManifest(final File scenarioFile) {

		return ServiceHelper.withOptionalService(IScenarioCipherProvider.class, scenarioCipherProvider -> {
			return loadManifest(scenarioFile, scenarioCipherProvider);
		});
	}

	public static @Nullable Manifest loadManifest(final File scenarioFile, IScenarioCipherProvider scenarioCipherProvider) {
		final URI fileURI = URI.createFileURI(scenarioFile.toString());

		final URI manifestURI = ScenarioStorageUtil.createArtifactURI(fileURI, ScenarioStorageUtil.PATH_MANIFEST_OBJECT);

		assert manifestURI != null;
		final ResourceSet resourceSet = ResourceHelper.createResourceSet(scenarioCipherProvider);
		assert resourceSet != null;

		try {
			final Resource resource = ResourceHelper.loadResource(resourceSet, manifestURI);
			if (resource.getContents().size() == 1) {
				final EObject top = resource.getContents().get(0);
				if (top instanceof Manifest) {
					return (Manifest) top;
				}
			}
		} catch (final Exception e) {
			// Unable to parse file for some reason
			log.debug("Unable to find manifest for " + scenarioFile, e);
		}
		return null;
	}

	public static Map<String, String> extractScenarioDataVersions(final Manifest manifest) {

		if (manifest != null) {
			return manifest.getModelDependencies().stream() //
					.filter(m -> m.getKey() != null) //
					.filter(m -> m.getDataVersion() != null) //
					.collect(Collectors.toMap(ModelArtifact::getKey, ModelArtifact::getDataVersion));

		}
		return null;
	}
}
