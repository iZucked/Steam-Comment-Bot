/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.migration.scenario;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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
	public void migrateScenario(@Nullable final IScenarioService scenarioService, @NonNull final ScenarioInstance scenarioInstance, final @NonNull IProgressMonitor monitor) throws Exception {
		monitor.beginTask("Migrate Scenario", 100);
		try {
			{
				String context = scenarioInstance.getVersionContext();
				if (context == null || context.isEmpty()) {
					context = migrationRegistry.getDefaultMigrationContext();
					scenarioInstance.setVersionContext(context);
					scenarioInstance.setScenarioVersion(0);
				}
			}
			{
				String context = scenarioInstance.getClientVersionContext();

				if (context == null || context.isEmpty()) {
					context = migrationRegistry.getDefaultClientMigrationContext();
					scenarioInstance.setClientVersionContext(context);
					scenarioInstance.setClientScenarioVersion(0);
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
					migrator.performMigration(scenarioService, scenarioInstance, new SubProgressMonitor(monitor, 100));
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
	public String getScenarioContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getScenarioVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getClientScenarioContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getClientScenarioVersion() {
		// TODO Auto-generated method stub
		return 0;
	}
}
