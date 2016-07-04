/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.liveeval;

import com.mmxlabs.scenario.service.model.ScenarioInstance;


/**
 * This is a bodge to make the evaluation handler from the platform available here.
 * 
 * @author hinton
 *
 */
public interface IScenarioInstanceEvaluator {
	public void evaluate(final ScenarioInstance instance);
}
