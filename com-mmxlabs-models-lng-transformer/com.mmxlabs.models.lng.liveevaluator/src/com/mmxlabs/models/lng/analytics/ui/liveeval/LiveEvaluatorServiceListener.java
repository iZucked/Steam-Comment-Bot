package com.mmxlabs.models.lng.analytics.ui.liveeval;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.scenario.service.IScenarioService;

public class LiveEvaluatorServiceListener {

	private final Map<IScenarioService, LiveEvaluatorScenarioServiceListener> evaluatorMap = new HashMap<IScenarioService, LiveEvaluatorScenarioServiceListener>();

	public void addScenarioService(IScenarioService scenarioService) {
		final LiveEvaluatorScenarioServiceListener listener = new LiveEvaluatorScenarioServiceListener();
		scenarioService.addScenarioServiceListener(listener);
		evaluatorMap.put(scenarioService, listener);
	}

	public void removeScenarioService(IScenarioService scenarioService) {
		final LiveEvaluatorScenarioServiceListener listener = evaluatorMap.get(scenarioService);
		scenarioService.removeScenarioServiceListener(listener);
	}
	
	
	public void setLiveEvaluatorEnabled(boolean enabled) {
		
	}
}
