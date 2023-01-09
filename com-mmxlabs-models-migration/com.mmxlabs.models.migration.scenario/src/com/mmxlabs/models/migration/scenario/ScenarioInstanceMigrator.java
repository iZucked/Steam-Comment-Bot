/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.migration.scenario;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.io.ByteStreams;
import com.mmxlabs.common.io.FileDeleter;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.migration.DataManifest;
import com.mmxlabs.models.migration.DataManifest.EObjectData;
import com.mmxlabs.models.migration.IClientMigrationUnit;
import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.manifest.ManifestFactory;
import com.mmxlabs.scenario.service.manifest.ModelArtifact;
import com.mmxlabs.scenario.service.manifest.StorageType;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ISharedDataModelType;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.util.ResourceHelper;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;

/**
 * The {@link ScenarioInstanceMigrator} controls the migration process for a {@link ScenarioInstance}. It will attempt to migrate a scenario if required. It will create a copy of the scenario data to
 * migrate and only overwrite the original when the migration process completes without an {@link Exception}. Note, incorrectly coded migration steps could still corrupt a scenario.
 * 
 */
public class ScenarioInstanceMigrator {

	private final IMigrationRegistry migrationRegistry;
	private final IScenarioCipherProvider scenarioCipherProvider;

	public ScenarioInstanceMigrator(final IMigrationRegistry migrationRegistry, final IScenarioCipherProvider scenarioCipherProvider) {
		this.migrationRegistry = migrationRegistry;
		this.scenarioCipherProvider = scenarioCipherProvider;
	}

	public void performMigration(final URI archiveURI, final Manifest manifest, final IProgressMonitor monitor) throws Exception {
		// Check inputs
		final String scenarioContext = manifest.getVersionContext();
		final String clientContext = manifest.getClientVersionContext();

		checkArgument(scenarioContext != null, "Scenario has no version context. Unable to migrate");
		// checkArgument(scenarioInstance.getInstance() == null, "Scenario already loaded. Unable to migrate");

		assert scenarioContext != null;

		final int latestScenarioVersion = migrationRegistry.getLatestContextVersion(scenarioContext);
		final int latestClientVersion = clientContext == null ? 0 : migrationRegistry.getLatestClientContextVersion(clientContext);
		int currentScenarioVersion = manifest.getScenarioVersion();
		int currentClientVersion = manifest.getClientScenarioVersion();

		final ExtensibleURIConverterImpl uc = new ExtensibleURIConverterImpl();

		// // Get original URI's as a list
		// final URI originalURI = scenarioService == null ? URI.createURI(subModelURI) : scenarioService.resolveURI(subModelURI);

		final List<File> tmpFiles = new ArrayList<>();
		monitor.beginTask("Migrate scenario", 100);
		try {

			// Copy data files for manipulation
			assert archiveURI != null;
			final File f = File.createTempFile("migration", ".lingo", ScenarioStorageUtil.getTempDirectory());
			tmpFiles.add(f);
			// Create a temp file and generate a URI to it to pass into migration code.
			final URI tmpArchiveURI = URI.createFileURI(f.getCanonicalPath());
			assert tmpArchiveURI != null;
			copyURIData(uc, archiveURI, tmpArchiveURI);

			@NonNull
			final URI tmpRootObjectURI = ScenarioStorageUtil.createArtifactURI(tmpArchiveURI, ScenarioStorageUtil.PATH_ROOT_OBJECT);

			final DataManifest dataManifest = new DataManifest(tmpArchiveURI, tmpRootObjectURI);

			final URI manifestURI = ScenarioStorageUtil.createArtifactURI(tmpArchiveURI, ScenarioStorageUtil.PATH_MANIFEST_OBJECT);
			{

				// final ResourceSet resourceSet = ResourceHelper.createResourceSet(scenarioCipherProvider);
				// final Resource r = resourceSet.getResource(manifestURI, true);
				//
				// final Manifest manifest = (Manifest) r.getContents().get(0);

				final List<ModelArtifact> modelDependencies = manifest.getModelDependencies();
				for (final ModelArtifact artifact : modelDependencies) {
					if (artifact.getStorageType() == StorageType.INTERNAL) {
						continue;
					} else if (artifact.getStorageType() == StorageType.COLOCATED && artifact.getType().equals("EOBJECT")) {
						final ISharedDataModelType<?> key = ISharedDataModelType.registry().lookup(artifact.getKey());
						dataManifest.add(key, artifact.getPath(), artifact.getDataVersion());
					} else {
						// Unsupported type
						assert false;
					}
				}
			}

			if (currentScenarioVersion < 0) {
				final int lastReleaseVersion = migrationRegistry.getLastReleaseVersion(scenarioContext);
				currentScenarioVersion = lastReleaseVersion;
			}
			if (currentClientVersion < 0) {
				assert clientContext != null;
				final int lastReleaseVersion = migrationRegistry.getLastReleaseClientVersion(clientContext);
				currentClientVersion = lastReleaseVersion;
			}
			// Apply Migration Chain
			final int[] migratedVersion = applyMigrationChain(scenarioContext, currentScenarioVersion, latestScenarioVersion, clientContext, currentClientVersion, latestClientVersion, dataManifest,
					SubMonitor.convert(monitor, 100));

			// Sanity check - can we load the new scenario without error?
			{
				// Construct a normal resource set. This will use the global package registry etc
				final ResourceSet resourceSet = ResourceHelper.createResourceSet(scenarioCipherProvider);

				try {
					// Create a sample instance object

					final Resource r = ResourceHelper.loadResource(resourceSet, tmpRootObjectURI);
					final Object submodel = r.getContents().get(0);
					if (submodel == null) {
						throw new RuntimeException("Error loading migrated scenario model. Aborting");
					}

				} catch (final Exception e) {
					throw new RuntimeException("Error loading migrated scenario. Aborting", e);
				}
			}
			// Make sure the migration has worked!
			if (migratedVersion[0] != latestScenarioVersion) {
				throw new ScenarioMigrationException(String.format("Scenario was not migrated to latest version. Expected %d, currently %d.", latestScenarioVersion, manifest.getScenarioVersion()));
			}
			// if (migratedVersion[1] != latestClientVersion) {
			// throw new ScenarioMigrationException(
			// String.format("Scenario was not migrated to latest client version. Expected %d, currently %d.", latestClientVersion, manifest.getClientScenarioVersion()));
			// }

			// Update manifest
			{

				final ResourceSet resourceSet = ResourceHelper.createResourceSet(scenarioCipherProvider);
				final Resource r = ResourceHelper.loadResource(resourceSet, manifestURI);

				r.getContents().clear();
				r.getContents().add(manifest);

				// final Manifest manifest = (Manifest) r.getContents().get(0);

				manifest.getModelDependencies().clear();

				for (final EObjectData d : dataManifest.getEObjectData()) {
					final ModelArtifact artifact = ManifestFactory.eINSTANCE.createModelArtifact();
					artifact.setDataVersion(d.getDataVersion());
					artifact.setKey(d.getKey().getID());
					artifact.setPath(d.getURIFragment());
					artifact.setType("EOBJECT");
					artifact.setStorageType(StorageType.COLOCATED);

					manifest.getModelDependencies().add(artifact);

				}
				manifest.setScenarioVersion(migratedVersion[0]);
				manifest.setClientScenarioVersion(migratedVersion[1]);
				ResourceHelper.saveResource(r);
			}

			// Copy back over original data
			{
				assert tmpArchiveURI != null;
				final URI uri = archiveURI;
				assert uri != null;
				// Use a new URI Convertor otherwise the previous map will cause source == dest!
				copyURIData(new ExtensibleURIConverterImpl(), tmpArchiveURI, uri);
			}
		} catch (final Exception e) {
			throw new ScenarioMigrationException(e);
		} finally {
			// Done! Clean up
			final boolean secureDelete = LicenseFeatures.isPermitted(FileDeleter.LICENSE_FEATURE__SECURE_DELETE);
			for (final File f : tmpFiles) {
				FileDeleter.delete(f, secureDelete);
			}

			monitor.done();
		}

	}

	/**
	 * Returns latest version number to store scenario against. This will be the value of the latestVersion param unless this is negative (snapshot) in which case the previous version number is
	 * returned.
	 * 
	 * @param dataManifest
	 * 
	 * @param context
	 * @param scenarioVersion
	 * @param latestVersion
	 * @param tmpURIs
	 * @param uc
	 * @return
	 * @throws Exception
	 */
	public int[] applyMigrationChain(@NonNull final String scenarioContext, final int currentScenarioVersion, final int latestScenarioVersion, @Nullable final String clientContext,
			final int currentClientVersion, final int latestClientVersion, final DataManifest dataManifest, final IProgressMonitor monitor) throws Exception {

		final List<IMigrationUnit> chain = migrationRegistry.getMigrationChain(scenarioContext, currentScenarioVersion, latestScenarioVersion, clientContext, currentClientVersion,
				latestClientVersion);

		int scenarioVersion = currentScenarioVersion;
		int clientVersion = currentClientVersion;
		monitor.beginTask("Migrate Scenario", chain.size());
		try {

			for (final IMigrationUnit unit : chain) {

				unit.migrate(Collections.<URI, PackageData> emptyMap(), dataManifest);

				if (unit instanceof IClientMigrationUnit clientMigrationUnit) {
					// Only return real version numbers - ignore snapshot versions
					if (clientMigrationUnit.getClientDestinationVersion() >= 0) {
						clientVersion = clientMigrationUnit.getClientDestinationVersion();
					}
				}

				// Only return real version numbers - ignore snapshot versions
				if (unit.getScenarioDestinationVersion() >= 0) {
					scenarioVersion = unit.getScenarioDestinationVersion();
				}
				monitor.worked(1);
			}
		} finally {
			monitor.done();
		}
		return new int[] { scenarioVersion, clientVersion };
	}

	public void copyURIData(@NonNull final URIConverter uc, @NonNull final URI sourceURI, @NonNull final URI destURI) throws IOException {

		// Get input stream from original URI
		try (InputStream is = uc.createInputStream(sourceURI)) {
			try (OutputStream os = uc.createOutputStream(destURI)) {
				ByteStreams.copy(is, os);
			}
		}
	}
}
