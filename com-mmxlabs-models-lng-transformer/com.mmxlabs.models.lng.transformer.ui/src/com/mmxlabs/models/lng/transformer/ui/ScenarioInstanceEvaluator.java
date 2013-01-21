/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import com.mmxlabs.models.lng.analytics.ui.liveeval.IScenarioInstanceEvaluator;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.lng.transformer.ui.navigator.handlers.StartOptimisationHandler;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;

public class ScenarioInstanceEvaluator implements IScenarioInstanceEvaluator {
	@Override
	public void evaluate(ScenarioInstance instance) {
		StartOptimisationHandler.evaluateScenarioInstance(Activator.getDefault().getJobManager(), instance, false, ScenarioLock.EVALUATOR);
	}
}
