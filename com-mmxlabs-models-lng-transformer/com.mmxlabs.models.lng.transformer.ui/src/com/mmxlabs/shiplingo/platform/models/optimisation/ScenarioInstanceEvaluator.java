package com.mmxlabs.shiplingo.platform.models.optimisation;

import com.mmxlabs.models.lng.analytics.ui.liveeval.IScenarioInstanceEvaluator;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.shiplingo.platform.models.optimisation.navigator.handlers.StartOptimisationHandler;

public class ScenarioInstanceEvaluator implements IScenarioInstanceEvaluator {
	@Override
	public void evaluate(ScenarioInstance instance) {
		StartOptimisationHandler.evaluateScenarioInstance(Activator.getDefault().getJobManager(), instance, false, ScenarioLock.EVALUATOR);
	}
}
