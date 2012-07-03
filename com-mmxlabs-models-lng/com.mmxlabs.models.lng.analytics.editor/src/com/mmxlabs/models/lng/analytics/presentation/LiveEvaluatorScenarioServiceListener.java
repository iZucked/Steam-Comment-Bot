package com.mmxlabs.models.lng.analytics.presentation;

import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.analytics.ui.liveeval.LiveEvaluator;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.impl.ScenarioServiceListener;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class LiveEvaluatorScenarioServiceListener extends ScenarioServiceListener {

	private final Map<ScheduleModel, LiveEvaluator> evaluatorMap = new WeakHashMap<ScheduleModel, LiveEvaluator>();

	@Override
	public void onPostScenarioInstanceLoad(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
		final EObject obj = scenarioInstance.getInstance();
		if (obj instanceof MMXRootObject) {
			final MMXRootObject rootObject = (MMXRootObject) obj;
			final ScheduleModel schedule = rootObject.getSubModel(ScheduleModel.class);
			if (schedule != null) {
				final LiveEvaluator evaluator = new LiveEvaluator(scenarioInstance);
				schedule.eAdapters().add(evaluator);
				evaluatorMap.put(schedule, evaluator);
			}
		}
	}

	public void dispose() {
		for (final Map.Entry<ScheduleModel, LiveEvaluator> e : evaluatorMap.entrySet()) {
			final ScheduleModel model = e.getKey();
			final LiveEvaluator eval = e.getValue();
			if (model != null && eval != null) {
				model.eAdapters().remove(eval);
			}
		}
		evaluatorMap.clear();
	}
}
