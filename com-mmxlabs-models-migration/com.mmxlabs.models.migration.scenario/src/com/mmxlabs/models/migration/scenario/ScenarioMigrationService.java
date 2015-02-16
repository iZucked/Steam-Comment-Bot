/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.migration.scenario;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.scenario.service.IScenarioMigrationService;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.util.encryption.IScenarioCipherProvider;

/**
 */
public class ScenarioMigrationService implements IScenarioMigrationService {

	private IMigrationRegistry migrationRegistry;
	private IScenarioCipherProvider scenarioCipherProvider;

	@Override
	public void migrateScenario(@NonNull final IScenarioService scenarioService, @NonNull final ScenarioInstance scenarioInstance) throws Exception {

		{
			String context = scenarioInstance.getVersionContext();
			if (context == null || context.isEmpty()) {
				context = migrationRegistry.getDefaultMigrationContext();
				scenarioInstance.setVersionContext(context);
				scenarioInstance.setScenarioVersion(0);
			}
		}
		boolean installClientModel = false;
		{
			final String context = scenarioInstance.getClientVersionContext();

			if (context == null || context.isEmpty()) {
				installClientModel = true;
			}
		}
		final String scenarioContext = scenarioInstance.getVersionContext();
		final String clientContext = scenarioInstance.getClientVersionContext();

		if (scenarioContext != null && getMigrationRegistry().getMigrationContexts().contains(scenarioContext)) {

			final int latestScenarioVersion = getMigrationRegistry().getLatestContextVersion(scenarioContext);
			final int latestClientVersion = clientContext == null ? 0 : getMigrationRegistry().getLatestClientContextVersion(clientContext);

			final int scenarioVersion = scenarioInstance.getScenarioVersion();
			final int clientVersion = clientContext == null ? 0 : scenarioInstance.getClientScenarioVersion();

			if (latestScenarioVersion < 0 || scenarioVersion < latestScenarioVersion || latestClientVersion < 0 || clientVersion < latestClientVersion) {

				final ScenarioInstanceMigrator migrator = new ScenarioInstanceMigrator(getMigrationRegistry(), getScenarioCipherProvider());
				migrator.performMigration(scenarioService, scenarioInstance);
			}
		}
		if (installClientModel) {
			final String ctx = migrationRegistry.getDefaultClientMigrationContext();
			assert ctx != null;
			scenarioInstance.setClientVersionContext(ctx);
			scenarioInstance.setClientScenarioVersion(migrationRegistry.getLastReleaseClientVersion(ctx));
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
}
