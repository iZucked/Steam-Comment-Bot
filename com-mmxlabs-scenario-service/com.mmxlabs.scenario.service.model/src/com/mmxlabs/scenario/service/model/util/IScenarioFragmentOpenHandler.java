/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

@NonNullByDefault
public interface IScenarioFragmentOpenHandler {

	void open(ScenarioFragment fragment, ScenarioInstance scenarioInstance);
}
