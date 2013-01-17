package com.mmxlabs.models.migration.scenario;

import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.scenario.service.IScenarioMigrationService;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ScenarioMigrationService implements IScenarioMigrationService {

	private IMigrationRegistry migrationRegistry;

	@Override
	public void migrateScenario(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) throws Exception {

		String context = scenarioInstance.getVersionContext();

		if (context == null || context.isEmpty()) {
			context = migrationRegistry.getDefaultMigrationContext();
			scenarioInstance.setVersionContext(context);
			scenarioInstance.setScenarioVersion(0);
		}

		if (getMigrationRegistry().getMigrationContexts().contains(context)) {

			final int latestVersion = getMigrationRegistry().getLatestContextVersion(context);

			final int scenarioVersion = scenarioInstance.getScenarioVersion();

			if (latestVersion == -1 || scenarioVersion < latestVersion) {

				final ScenarioInstanceMigrator migrator = new ScenarioInstanceMigrator(getMigrationRegistry());
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
