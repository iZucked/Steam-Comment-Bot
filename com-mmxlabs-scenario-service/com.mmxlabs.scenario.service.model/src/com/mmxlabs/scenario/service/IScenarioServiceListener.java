/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scenario.service;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * A listener interface to notify on various {@link IScenarioService} events.
 * 
 * @author Simon Goodall
 * 
 */
@NonNullByDefault
public interface IScenarioServiceListener {

	/**
	 * Callback fired before a {@link ScenarioInstance} is deleted via {@link IScenarioService#delete(com.mmxlabs.scenario.service.model.Container)}
	 * 
	 * @param scenarioService
	 * @param scenarioInstance
	 */
	default void onPreScenarioInstanceDelete(IScenarioService scenarioService, ScenarioInstance scenarioInstance) {
	}

	/**
	 * Callback fired after a {@link ScenarioInstance} has been unloaded via {@link IScenarioService#save(ScenarioInstance)}. NOTE: Unload is currently not part of API
	 * 
	 * @param scenarioService
	 * @param scenarioInstance
	 */
	default void onPreScenarioInstanceUnload(IScenarioService scenarioService, ScenarioInstance scenarioInstance) {
	}
}
