/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import org.eclipse.core.runtime.SubProgressMonitor;
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
import com.mmxlabs.models.migration.IClientMigrationUnit;
import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.util.ResourceHelper;
import com.mmxlabs.scenario.service.util.encryption.IScenarioCipherProvider;

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

	public void performMigration(@NonNull final IScenarioService scenarioService, @NonNull final ScenarioInstance scenarioInstance, IProgressMonitor monitor) throws Exception {
		// Check inputs
		final String scenarioContext = scenarioInstance.getVersionContext();
		final String clientContext = scenarioInstance.getClientVersionContext();

		checkArgument(scenarioContext != null, "Scenario has no version context. Unable to migrate");
		checkArgument(scenarioInstance.getInstance() == null, "Scenario already loaded. Unable to migrate");

		assert scenarioContext != null;

		final int latestScenarioVersion = migrationRegistry.getLatestContextVersion(scenarioContext);
		final int latestClientVersion = clientContext == null ? 0 : migrationRegistry.getLatestClientContextVersion(clientContext);
		int currentScenarioVersion = scenarioInstance.getScenarioVersion();
		int currentClientVersion = scenarioInstance.getClientScenarioVersion();

		final String subModelURI = scenarioInstance.getRootObjectURI();
		assert subModelURI != null;

		final ExtensibleURIConverterImpl uc = new ExtensibleURIConverterImpl();

		// Get original URI's as a list
		final URI originalURI = scenarioService.resolveURI(subModelURI);

		final List<File> tmpFiles = new ArrayList<File>();
		monitor.beginTask("Migrate scenario", 100);
		try {
			// Copy data files for manipulation
			assert originalURI != null;
			final File f = File.createTempFile("migration", ".xmi");
			tmpFiles.add(f);
			// Create a temp file and generate a URI to it to pass into migration code.
			final URI tmpURI = URI.createFileURI(f.getCanonicalPath());
			assert tmpURI != null;
			copyURIData(uc, originalURI, tmpURI);

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
			final int[] migratedVersion = applyMigrationChain(scenarioContext, currentScenarioVersion, latestScenarioVersion, clientContext, currentClientVersion, latestClientVersion, tmpURI,
					new SubProgressMonitor(monitor, 100));

			// Sanity check - can we load the new scenario without error?
			{
				// Construct a normal resource set. This will use the global package registry etc
				final ResourceSet resourceSet = ResourceHelper.createResourceSet(scenarioCipherProvider);

				try {
					// Create a sample instance object
					final Resource r = ResourceHelper.loadResource(resourceSet, tmpURI);
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
				throw new ScenarioMigrationException(
						String.format("Scenario was not migrated to latest version. Expected %d, currently %d.", latestScenarioVersion, scenarioInstance.getScenarioVersion()));
			}
			if (migratedVersion[1] != latestClientVersion) {
				throw new ScenarioMigrationException(
						String.format("Scenario was not migrated to latest client version. Expected %d, currently %d.", latestClientVersion, scenarioInstance.getClientScenarioVersion()));
			}

			// Copy back over original data
			{
				assert tmpURI != null;
				final URI uri = originalURI;
				assert uri != null;
				// Use a new URI Convertor otherwise the previous map will cause source == dest!
				copyURIData(new ExtensibleURIConverterImpl(), tmpURI, uri);
			}

			scenarioInstance.setScenarioVersion(migratedVersion[0]);
			scenarioInstance.setClientScenarioVersion(migratedVersion[1]);
		} catch (Exception e) {
			throw new ScenarioMigrationException(e);
		} finally {
			// Done! Clean up
			boolean secureDelete = LicenseFeatures.isPermitted("features:secure-delete");
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
	 * @param context
	 * @param scenarioVersion
	 * @param latestVersion
	 * @param tmpURIs
	 * @param uc
	 * @return
	 * @throws Exception
	 */
	public int[] applyMigrationChain(@NonNull final String scenarioContext, final int currentScenarioVersion, final int latestScenarioVersion, @Nullable final String clientContext,
			final int currentClientVersion, final int latestClientVersion, @NonNull final URI tmpURI, IProgressMonitor monitor) throws Exception {

		final List<IMigrationUnit> chain = migrationRegistry.getMigrationChain(scenarioContext, currentScenarioVersion, latestScenarioVersion, clientContext, currentClientVersion,
				latestClientVersion);

		int scenarioVersion = currentScenarioVersion;
		int clientVersion = currentClientVersion;
		monitor.beginTask("Migrate Scenario", chain.size());
		try {
			for (final IMigrationUnit unit : chain) {

				unit.migrate(tmpURI, Collections.<URI, PackageData> emptyMap());

				if (unit instanceof IClientMigrationUnit) {
					final IClientMigrationUnit clientMigrationUnit = (IClientMigrationUnit) unit;
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
		InputStream is = null;
		OutputStream os = null;
		try {

			// Get input stream from original URI
			is = uc.createInputStream(sourceURI);
			os = uc.createOutputStream(destURI);

			ByteStreams.copy(is, os);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (final IOException e) {

				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (final IOException e) {

				}
			}
		}
	}
}
