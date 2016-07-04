/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * An interface to provide {@link ScenarioInstance}s.
 * 
 * @author Simon Goodall
 * 
 */
public interface IScenarioInstanceProvider {
	public ScenarioInstance getScenarioInstance();
}
