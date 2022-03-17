/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scenario.service;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * A listener interface to notify on various {@link IScenarioService} events.
 * 
 * @author Simon Goodall
 * 
 */
public interface IScenarioServiceListener {

	/**
	 * Callback fired before a {@link ScenarioInstance} is deleted via {@link IScenarioService#delete(com.mmxlabs.scenario.service.model.Container)}
	 * 
	 * @param scenarioService
	 * @param scenarioInstance
	 */
	public void onPreScenarioInstanceDelete(@NonNull IScenarioService scenarioService, @NonNull ScenarioInstance scenarioInstance);

	/**
	 * Callback fired after a {@link ScenarioInstance} has been unloaded via {@link IScenarioService#save(ScenarioInstance)}. NOTE: Unload is currently not part of API
	 * 
	 * @param scenarioService
	 * @param scenarioInstance
	 */
	void onPreScenarioInstanceUnload(@NonNull IScenarioService scenarioService, @NonNull ScenarioInstance scenarioInstance);
}
