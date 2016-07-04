/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.impl;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.IScenarioServiceListener;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Empty implementation of {@link IScenarioServiceListener} intended to be subclassed to allow clients to implement only the methods they are interested in.
 * 
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author Simon Goodall
 * 
 */
public class ScenarioServiceListener implements IScenarioServiceListener {

	@Override
	public void onPreScenarioInstanceLoad(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

	}

	@Override
	public void onPostScenarioInstanceLoad(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

	}

	@Override
	public void onPreScenarioInstanceSave(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

	}

	@Override
	public void onPostScenarioInstanceSave(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

	}

	@Override
	public void onPreScenarioInstanceDelete(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

	}

	@Override
	public void onPostScenarioInstanceDelete(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

	}

	@Override
	public void onPreScenarioInstanceUnload(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

	}

	@Override
	public void onPostScenarioInstanceUnload(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

	}
}
