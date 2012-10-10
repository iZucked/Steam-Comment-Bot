/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.liveeval.internal;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.analytics.ui.liveeval.IScenarioInstanceEvaluator;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.impl.ScenarioServiceListener;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class LiveEvaluatorScenarioServiceListener extends ScenarioServiceListener {

	private final Map<ScenarioInstance, LiveEvaluator> evaluatorMap = new WeakHashMap<ScenarioInstance, LiveEvaluator>();

	private final boolean enabled;

	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	private IScenarioInstanceEvaluator scenarioInstanceEvaluator;

	public LiveEvaluatorScenarioServiceListener(final boolean enabled) {
		this.enabled = enabled;
	}

	public LiveEvaluatorScenarioServiceListener() {
		this(true);
	}

	public void hookExisting(final IScenarioService scenarioService) {
		hookExisting(scenarioService.getServiceModel());
	}

	private void hookExisting(final Container container) {
		for (final Container c : container.getElements()) {
			if (c instanceof ScenarioInstance) {
				final ScenarioInstance scenarioInstance = (ScenarioInstance) c;
				final EObject obj = scenarioInstance.getInstance();
				if (obj instanceof MMXRootObject) {
					final MMXRootObject rootObject = (MMXRootObject) obj;
					final ScheduleModel schedule = rootObject.getSubModel(ScheduleModel.class);
					if (schedule != null) {
						final LiveEvaluator evaluator = new LiveEvaluator(scenarioInstance, executor);
						evaluator.setEnabled(enabled);
						schedule.eAdapters().add(evaluator);
						scenarioInstance.eAdapters().add(evaluator);
						evaluatorMap.put(scenarioInstance, evaluator);
					}
				}
			}
			hookExisting(c);
		}
	}

	@Override
	public void onPostScenarioInstanceLoad(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
		final EObject obj = scenarioInstance.getInstance();
		if (obj instanceof MMXRootObject) {
			final MMXRootObject rootObject = (MMXRootObject) obj;
			final ScheduleModel schedule = rootObject.getSubModel(ScheduleModel.class);
			if (schedule != null) {
				final LiveEvaluator evaluator = new LiveEvaluator(scenarioInstance, executor);
				evaluator.setScenarioInstanceEvaluator(scenarioInstanceEvaluator);
				evaluator.setEnabled(enabled);
				schedule.eAdapters().add(evaluator);
				scenarioInstance.eAdapters().add(evaluator);
				evaluatorMap.put(scenarioInstance, evaluator);
			}
		}
	}

	@Override
	public void onPreScenarioInstanceDelete(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
		onPreScenarioInstanceUnload(scenarioService, scenarioInstance);
	}

	@Override
	public void onPreScenarioInstanceUnload(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
		final LiveEvaluator eval = evaluatorMap.get(scenarioInstance);
		final EObject obj = scenarioInstance.getInstance();

		if (scenarioInstance != null && eval != null) {
			scenarioInstance.eAdapters().remove(eval);
			if (obj instanceof MMXRootObject) {
				final MMXRootObject rootObject = (MMXRootObject) obj;
				final ScheduleModel schedule = rootObject.getSubModel(ScheduleModel.class);
				if (schedule != null) {
					schedule.eAdapters().remove(eval);
				}
			}
		}
		evaluatorMap.remove(scenarioInstance);
	}

	public void dispose() {
		for (final Map.Entry<ScenarioInstance, LiveEvaluator> e : evaluatorMap.entrySet()) {
			final ScenarioInstance scenarioInstance = e.getKey();
			final LiveEvaluator eval = e.getValue();
			if (scenarioInstance != null && eval != null) {
				scenarioInstance.eAdapters().remove(eval);
				final EObject obj = scenarioInstance.getInstance();
				if (obj instanceof MMXRootObject) {
					final MMXRootObject rootObject = (MMXRootObject) obj;
					final ScheduleModel schedule = rootObject.getSubModel(ScheduleModel.class);
					if (schedule != null) {
						schedule.eAdapters().remove(eval);
					}
				}
			}
		}
		evaluatorMap.clear();
	}

	public void setEnabled(final boolean enabled) {
		for (final LiveEvaluator evaluator : evaluatorMap.values()) {
			evaluator.setEnabled(enabled);
		}
	}

	public IScenarioInstanceEvaluator getScenarioInstanceEvaluator() {
		return scenarioInstanceEvaluator;
	}

	public void setScenarioInstanceEvaluator(final IScenarioInstanceEvaluator scenarioInstanceEvaluator) {
		this.scenarioInstanceEvaluator = scenarioInstanceEvaluator;

		this.scenarioInstanceEvaluator = scenarioInstanceEvaluator;
		for (final LiveEvaluator evaluator : evaluatorMap.values()) {
			evaluator.setScenarioInstanceEvaluator(scenarioInstanceEvaluator);
		}
	}
}
