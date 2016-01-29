/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.liveeval.internal;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.models.lng.analytics.ui.liveeval.ILiveEvaluatorService;
import com.mmxlabs.models.lng.analytics.ui.liveeval.IScenarioInstanceEvaluator;
import com.mmxlabs.scenario.service.IScenarioService;

public class LiveEvaluatorServiceListener implements ILiveEvaluatorService {

	private final Map<IScenarioService, LiveEvaluatorScenarioServiceListener> evaluatorMap = new HashMap<IScenarioService, LiveEvaluatorScenarioServiceListener>();

	private boolean enabled = true;

	private IScenarioInstanceEvaluator scenarioInstanceEvaluator;

	public void addScenarioService(final IScenarioService scenarioService) {
		final LiveEvaluatorScenarioServiceListener listener = new LiveEvaluatorScenarioServiceListener(enabled);
		listener.setScenarioInstanceEvaluator(scenarioInstanceEvaluator);
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

	public IScenarioInstanceEvaluator getScenarioInstanceEvaluator() {
		return scenarioInstanceEvaluator;
	}

	public void setScenarioInstanceEvaluator(final IScenarioInstanceEvaluator scenarioInstanceEvaluator) {
		this.scenarioInstanceEvaluator = scenarioInstanceEvaluator;

		this.scenarioInstanceEvaluator = scenarioInstanceEvaluator;
		for (final LiveEvaluatorScenarioServiceListener evaluator : evaluatorMap.values()) {
			evaluator.setScenarioInstanceEvaluator(scenarioInstanceEvaluator);
		}
	}
}
