/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * A listener interface to notify on various {@link IScenarioService} events.
 * 
 * @author Simon Goodall
 * 
 */
public interface IScenarioServiceListener {

	/**
	 * Callback fired before a {@link ScenarioInstance} has been loaded via {@link IScenarioService#load(ScenarioInstance)}. Note this is only invoked when a model is actually loaded, not just when
	 * the method is invoked.
	 * 
	 * @param scenarioService
	 * @param scenarioInstance
	 */
	public void onPreScenarioInstanceLoad(IScenarioService scenarioService, ScenarioInstance scenarioInstance);

	/**
	 * Callback fired after a {@link ScenarioInstance} has been loaded via {@link IScenarioService#load(ScenarioInstance)}. Note this is only invoked when a model is actually loaded, not just when the
	 * method is invoked.
	 * 
	 * @param scenarioService
	 * @param scenarioInstance
	 */
	public void onPostScenarioInstanceLoad(IScenarioService scenarioService, ScenarioInstance scenarioInstance);

	/**
	 * Callback fired before a {@link ScenarioInstance} has been loaded via {@link IScenarioService#save(ScenarioInstance)}
	 * 
	 * @param scenarioService
	 * @param scenarioInstance
	 */
	public void onPreScenarioInstanceSave(IScenarioService scenarioService, ScenarioInstance scenarioInstance);

	/**
	 * Callback fired after a {@link ScenarioInstance} has been loaded via {@link IScenarioService#save(ScenarioInstance)}
	 * 
	 * @param scenarioService
	 * @param scenarioInstance
	 */
	public void onPostScenarioInstanceSave(IScenarioService scenarioService, ScenarioInstance scenarioInstance);

	/**
	 * Callback fired before a {@link ScenarioInstance} is deleted via {@link IScenarioService#delete(com.mmxlabs.scenario.service.model.Container)}
	 * 
	 * @param scenarioService
	 * @param scenarioInstance
	 */
	public void onPreScenarioInstanceDelete(IScenarioService scenarioService, ScenarioInstance scenarioInstance);

	/**
	 * Callback fired after a {@link ScenarioInstance} is deleted via {@link IScenarioService#delete(com.mmxlabs.scenario.service.model.Container)}
	 * 
	 * @param scenarioService
	 * @param scenarioInstance
	 */
	public void onPostScenarioInstanceDelete(IScenarioService scenarioService, ScenarioInstance scenarioInstance);

	/**
	 * Callback fired after a {@link ScenarioInstance} has been unloaded via {@link IScenarioService#save(ScenarioInstance)}. NOTE: Unload is currently not part of API
	 * 
	 * @param scenarioService
	 * @param scenarioInstance
	 */
	void onPreScenarioInstanceUnload(IScenarioService scenarioService, ScenarioInstance scenarioInstance);

	/**
	 * Callback fired after a {@link ScenarioInstance} has been unloaded via {@link IScenarioService#save(ScenarioInstance)}. NOTE: Unload is currently not part of API
	 * 
	 * @param scenarioService
	 * @param scenarioInstance
	 */
	void onPostScenarioInstanceUnload(IScenarioService scenarioService, ScenarioInstance scenarioInstance);
}
