/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.migration.scenario;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.scenario.service.IScenarioMigrationService;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * @since 2.0
 */
public class ScenarioMigrationService implements IScenarioMigrationService {

	private IMigrationRegistry migrationRegistry;

	@Override
	public void migrateScenario(@NonNull final IScenarioService scenarioService, @NonNull final ScenarioInstance scenarioInstance) throws Exception {

		String context = scenarioInstance.getVersionContext();

		if (context == null || context.isEmpty()) {
			context = migrationRegistry.getDefaultMigrationContext();
			scenarioInstance.setVersionContext(context);
			scenarioInstance.setScenarioVersion(0);
		}

		if (context != null && getMigrationRegistry().getMigrationContexts().contains(context)) {

			final int latestVersion = getMigrationRegistry().getLatestContextVersion(context);

			final int scenarioVersion = scenarioInstance.getScenarioVersion();

			if (latestVersion < 0 || scenarioVersion < latestVersion) {

				final ScenarioInstanceMigrator migrator = new ScenarioInstanceMigrator(getMigrationRegistry());
				// Disable for now, fix up later
				 migrator.performMigration(scenarioService, scenarioInstance);
			}
		}
	}

	public IMigrationRegistry getMigrationRegistry() {
		return migrationRegistry;
	}

	public void setMigrationRegistry(final IMigrationRegistry migrationRegistry) {
		this.migrationRegistry = migrationRegistry;
	}
}
