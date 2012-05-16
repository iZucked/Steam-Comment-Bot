package com.mmxlabs.shiplingo.platform.models.optimisation;

import com.mmxlabs.models.lng.analytics.ui.liveeval.IScenarioInstanceEvaluator;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.shiplingo.platform.models.optimisation.navigator.handlers.StartOptimisationHandler;

public class ScenarioInstanceEvaluator implements IScenarioInstanceEvaluator {
	final StartOptimisationHandler handler = new StartOptimisationHandler(false);
	@Override
	public void evaluate(ScenarioInstance instance) {
		handler.evaluateScenarioInstance(Activator.getDefault().getJobManager(), instance);
	}
}
