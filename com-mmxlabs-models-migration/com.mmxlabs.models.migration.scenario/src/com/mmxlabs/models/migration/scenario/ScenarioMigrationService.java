package com.mmxlabs.models.migration.scenario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.scenario.service.IScenarioMigrationService;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ScenarioMigrationService implements IScenarioMigrationService {

	private static final Logger log = LoggerFactory.getLogger(ScenarioMigrationService.class);

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
				try {
					migrator.performMigration(scenarioService, scenarioInstance);
				} catch (final Exception e) {
					log.error(e.getMessage(), e);
				}

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
