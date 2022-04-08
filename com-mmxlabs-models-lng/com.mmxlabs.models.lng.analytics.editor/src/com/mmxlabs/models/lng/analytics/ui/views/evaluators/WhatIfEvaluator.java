/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.util.function.Consumer;

import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class WhatIfEvaluator {

	public static void runSandbox(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel model) {

		ScenarioInstance scenarioInstance = scenarioEditingLocation.getScenarioInstance();
		Consumer<IAnalyticsScenarioEvaluator> f1 = evaluator -> evaluator.runSandbox(scenarioInstance, model);
		ServiceHelper.withServiceConsumer(IAnalyticsScenarioEvaluator.class, f1);
	}
}
