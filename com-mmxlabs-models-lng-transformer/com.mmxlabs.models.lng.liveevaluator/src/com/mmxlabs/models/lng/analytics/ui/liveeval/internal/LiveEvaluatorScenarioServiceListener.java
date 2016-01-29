/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.liveeval.internal;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.analytics.ui.liveeval.IScenarioInstanceEvaluator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.impl.ScenarioServiceListener;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class LiveEvaluatorScenarioServiceListener extends ScenarioServiceListener {

	/**
	 * A mapping between a {@link ScenarioInstance} and a {@link LiveEvaluator}. Note, we need to synchronize access to avoid concurrent modifications, or even multiple {@link LiveEvaluator}s for a
	 * single {@link ScenarioInstance}.
	 */
	private final Map<ScenarioInstance, LiveEvaluator> evaluatorMap = new WeakHashMap<ScenarioInstance, LiveEvaluator>();

	private boolean enabled;

	private final ExecutorService executor = Executors.newSingleThreadExecutor();
	private final ExecutorService hookExecutor = Executors.newSingleThreadExecutor();

	private IScenarioInstanceEvaluator scenarioInstanceEvaluator;

	public LiveEvaluatorScenarioServiceListener(final boolean enabled) {
		this.enabled = enabled;
	}

	public LiveEvaluatorScenarioServiceListener() {
		this(true);
	}

	public void hookExisting(final IScenarioService scenarioService) {
		// getServiceModel can block for a while causing issues with the service framework. So fire off in a thread.
		hookExecutor.execute(new Runnable() {
			@Override
			public void run() {
				hookExisting(scenarioService.getServiceModel());
			}
		});
	}

	private void hookExisting(final Container container) {
		for (final Container c : container.getElements()) {
			if (c instanceof ScenarioInstance) {
				final ScenarioInstance scenarioInstance = (ScenarioInstance) c;
				final ScheduleModel schedule = getScheduleModel(scenarioInstance);

				if (schedule != null) {
					// Synchronize to avoid overwriting concurrently created LiveEvaluators
					synchronized (evaluatorMap) {
						if (!evaluatorMap.containsKey(scenarioInstance)) {

							final LiveEvaluator evaluator = new LiveEvaluator(scenarioInstance, executor);
							evaluator.setEnabled(enabled);
							schedule.eAdapters().add(evaluator);
							scenarioInstance.eAdapters().add(evaluator);
							evaluatorMap.put(scenarioInstance, evaluator);
						}
					}
				}
			}
			hookExisting(c);
		}
	}

	@Override
	public void onPostScenarioInstanceLoad(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
		final ScheduleModel schedule = getScheduleModel(scenarioInstance);

		if (schedule != null) {
			// Synchronize to avoid overwriting concurrently created LiveEvaluators
			synchronized (evaluatorMap) {
				if (!evaluatorMap.containsKey(scenarioInstance)) {
					final LiveEvaluator evaluator = new LiveEvaluator(scenarioInstance, executor);
					evaluator.setScenarioInstanceEvaluator(scenarioInstanceEvaluator);
					evaluator.setEnabled(enabled);
					schedule.eAdapters().add(evaluator);
					scenarioInstance.eAdapters().add(evaluator);
					evaluatorMap.put(scenarioInstance, evaluator);
				}
			}
		}
	}

	@Override
	public void onPreScenarioInstanceDelete(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
		onPreScenarioInstanceUnload(scenarioService, scenarioInstance);
	}

	@Override
	public void onPreScenarioInstanceUnload(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
		synchronized (evaluatorMap) {
			final LiveEvaluator eval = evaluatorMap.get(scenarioInstance);
			if (scenarioInstance != null && eval != null) {
				scenarioInstance.eAdapters().remove(eval);
				final ScheduleModel schedule = getScheduleModel(scenarioInstance);
				if (schedule != null) {
					schedule.eAdapters().remove(eval);
				}
			}
			evaluatorMap.remove(scenarioInstance);
		}
	}

	private ScheduleModel getScheduleModel(final ScenarioInstance scenarioInstance) {
		if (scenarioInstance != null) {
			final EObject obj = scenarioInstance.getInstance();
			if (obj instanceof LNGScenarioModel) {
				final LNGScenarioModel rootObject = (LNGScenarioModel) obj;
				final ScheduleModel scheduleModel = rootObject.getScheduleModel();
				return scheduleModel;
			}
		}
		return null;
	}

	public void dispose() {
		for (final Map.Entry<ScenarioInstance, LiveEvaluator> e : evaluatorMap.entrySet()) {
			final ScenarioInstance scenarioInstance = e.getKey();
			final LiveEvaluator eval = e.getValue();
			if (scenarioInstance != null && eval != null) {
				scenarioInstance.eAdapters().remove(eval);
				final ScheduleModel schedule = getScheduleModel(scenarioInstance);
				if (schedule != null) {
					schedule.eAdapters().remove(eval);
				}
			}
		}
		evaluatorMap.clear();

		executor.shutdown();
		hookExecutor.shutdown();
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
		synchronized (evaluatorMap) {
			for (final LiveEvaluator evaluator : evaluatorMap.values()) {
				evaluator.setEnabled(enabled);
			}
		}
	}

	public IScenarioInstanceEvaluator getScenarioInstanceEvaluator() {
		return scenarioInstanceEvaluator;
	}

	public void setScenarioInstanceEvaluator(final IScenarioInstanceEvaluator scenarioInstanceEvaluator) {
		this.scenarioInstanceEvaluator = scenarioInstanceEvaluator;

		this.scenarioInstanceEvaluator = scenarioInstanceEvaluator;
		synchronized (evaluatorMap) {
			for (final LiveEvaluator evaluator : evaluatorMap.values()) {
				evaluator.setScenarioInstanceEvaluator(scenarioInstanceEvaluator);
			}
		}
	}
}
