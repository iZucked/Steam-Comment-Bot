/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * An interface specifying a service to handle scenario migration. Scenario services should require an instance of this service to be available to ensure scenarios can be properly loaded.
 * 
 * @since 3.0
 */
public interface IScenarioMigrationService {

	/**
	 * Attempt to migrate scenario to latest version. This method will do nothing if the scenario is already at the latest version. Any errors during a migration will an {@link Exception} of some
	 * kind.
	 * 
	 * @param scenarioService
	 * @param scenarioInstance
	 */
	void migrateScenario(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) throws Exception;
}
