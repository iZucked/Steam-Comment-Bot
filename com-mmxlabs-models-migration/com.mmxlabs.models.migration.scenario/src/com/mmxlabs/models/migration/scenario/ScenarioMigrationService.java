/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.migration.scenario;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.model.manager.IScenarioMigrationService;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;

/**
 */
public class ScenarioMigrationService implements IScenarioMigrationService {

	private IMigrationRegistry migrationRegistry;
	private IScenarioCipherProvider scenarioCipherProvider;

	@Override
	public void migrateScenario(URI archiveURI, @NonNull final Manifest manifest, final @NonNull IProgressMonitor monitor) throws Exception {
		monitor.beginTask("Migrate Scenario", 100);
		try {
			{
				String context = manifest.getVersionContext();
				if (context == null || context.isEmpty()) {
					context = migrationRegistry.getDefaultMigrationContext();
					manifest.setVersionContext(context);
					manifest.setScenarioVersion(0);
				}
			}
			{
				String context = manifest.getClientVersionContext();

				if (context == null || context.isEmpty()) {
					context = migrationRegistry.getDefaultClientMigrationContext();
					manifest.setClientVersionContext(context);
					manifest.setClientScenarioVersion(0);
				}
			}
			final String scenarioContext = manifest.getVersionContext();
			final String clientContext = manifest.getClientVersionContext();

			if (scenarioContext != null && getMigrationRegistry().getMigrationContexts().contains(scenarioContext)) {

				final int latestScenarioVersion = getMigrationRegistry().getLatestContextVersion(scenarioContext);
				final int latestClientVersion = clientContext == null ? 0 : getMigrationRegistry().getLatestClientContextVersion(clientContext);

				final int scenarioVersion = manifest.getScenarioVersion();
				final int clientVersion = clientContext == null ? 0 : manifest.getClientScenarioVersion();

				if (latestScenarioVersion < 0 || scenarioVersion < latestScenarioVersion || latestClientVersion < 0 || clientVersion < latestClientVersion) {

					final ScenarioInstanceMigrator migrator = new ScenarioInstanceMigrator(getMigrationRegistry(), getScenarioCipherProvider());
					migrator.performMigration(archiveURI, manifest, new SubProgressMonitor(monitor, 100));
				}
			}
		} finally {
			monitor.done();
		}
	}

	public IMigrationRegistry getMigrationRegistry() {
		return migrationRegistry;
	}

	public void setMigrationRegistry(final IMigrationRegistry migrationRegistry) {
		this.migrationRegistry = migrationRegistry;
	}

	public IScenarioCipherProvider getScenarioCipherProvider() {
		return scenarioCipherProvider;
	}

	public void setScenarioCipherProvider(final IScenarioCipherProvider scenarioCipherProvider) {
		this.scenarioCipherProvider = scenarioCipherProvider;
	}

	@Override
	public boolean needsMigrating(URI archiveURI, @NonNull Manifest manifest) {
		{
			String context = manifest.getVersionContext();
			if (context == null || context.isEmpty()) {
				context = migrationRegistry.getDefaultMigrationContext();
				manifest.setVersionContext(context);
				manifest.setScenarioVersion(0);
			}
		}
		{
			String context = manifest.getClientVersionContext();

			if (context == null || context.isEmpty()) {
				context = migrationRegistry.getDefaultClientMigrationContext();
				manifest.setClientVersionContext(context);
				manifest.setClientScenarioVersion(0);
			}
		}
		final String scenarioContext = manifest.getVersionContext();
		final String clientContext = manifest.getClientVersionContext();

		if (scenarioContext != null && getMigrationRegistry().getMigrationContexts().contains(scenarioContext)) {
			final int latestScenarioVersion = getMigrationRegistry().getLatestContextVersion(scenarioContext);
			final int latestClientVersion = clientContext == null ? 0 : getMigrationRegistry().getLatestClientContextVersion(clientContext);

			final int scenarioVersion = manifest.getScenarioVersion();
			final int clientVersion = clientContext == null ? 0 : manifest.getClientScenarioVersion();

			if (latestScenarioVersion < 0 || scenarioVersion < latestScenarioVersion || latestClientVersion < 0 || clientVersion < latestClientVersion) {
				return true;
			}
		}

		return false;
	}
}
