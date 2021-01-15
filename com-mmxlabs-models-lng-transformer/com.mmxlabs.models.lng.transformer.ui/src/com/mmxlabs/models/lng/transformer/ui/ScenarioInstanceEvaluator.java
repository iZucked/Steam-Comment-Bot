/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.ui.liveeval.IScenarioInstanceEvaluator;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class ScenarioInstanceEvaluator implements IScenarioInstanceEvaluator {
	@Override
	public void evaluate(final @NonNull ScenarioModelRecord modelRecord) {

		modelRecord.executeWithProvider(scenarioDataProvider -> {

			final EObject object = scenarioDataProvider.getScenario();
			if (object instanceof LNGScenarioModel) {
				final LNGScenarioModel scenarioModel = (LNGScenarioModel) object;
				scenarioDataProvider.setLastEvaluationFailed(true);

				final OptimisationPlan p = OptimisationHelper.getOptimiserSettings(scenarioModel, true, null, false, false, null);
				if (p == null) {
					return;
				}

				final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
						.withThreadCount(1) //
						.withOptimisationPlan(p) //
						.buildDefaultRunner();
				try {
					runnerBuilder.evaluateInitialState();
					scenarioDataProvider.setLastEvaluationFailed(false);
				} finally {
					runnerBuilder.dispose();
				}
			}
		});
	}
}
