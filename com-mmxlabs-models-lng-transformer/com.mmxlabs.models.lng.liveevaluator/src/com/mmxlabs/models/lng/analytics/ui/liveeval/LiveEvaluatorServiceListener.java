/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.liveeval;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.scenario.service.IScenarioService;

public class LiveEvaluatorServiceListener implements ILiveEvaluatorService {

	private final Map<IScenarioService, LiveEvaluatorScenarioServiceListener> evaluatorMap = new HashMap<IScenarioService, LiveEvaluatorScenarioServiceListener>();

	private boolean enabled = true;

	public void addScenarioService(final IScenarioService scenarioService) {
		final LiveEvaluatorScenarioServiceListener listener = new LiveEvaluatorScenarioServiceListener(enabled);
		scenarioService.addScenarioServiceListener(listener);
		evaluatorMap.put(scenarioService, listener);
		listener.hookExisting(scenarioService);
	}

	public void removeScenarioService(final IScenarioService scenarioService) {
		final LiveEvaluatorScenarioServiceListener listener = evaluatorMap.get(scenarioService);
		scenarioService.removeScenarioServiceListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.lng.analytics.ui.liveeval.ILiveEvaulatorService#setLiveEvaluatorEnabled(boolean)
	 */
	@Override
	public void setLiveEvaluatorEnabled(final boolean enabled) {

		this.enabled = false;
		for (final LiveEvaluatorScenarioServiceListener l : evaluatorMap.values()) {
			l.setEnabled(enabled);
		}
	}
}
