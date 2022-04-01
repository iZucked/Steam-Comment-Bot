/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.sandbox;

import java.util.function.Consumer;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;

import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.rcp.common.ServiceHelper;

public abstract class AbstractSandboxTestCase extends AbstractMicroTestCase {

	protected void evaluateSandbox(@NonNull OptionAnalysisModel model) {
		final LNGScenarioModel root = ScenarioModelUtil.findScenarioModel(model);
		assert root != null;

		Consumer<IAnalyticsScenarioEvaluator> func = evaluator -> evaluator.runSandbox(scenarioDataProvider, null, model, model::setResults, false, new NullProgressMonitor());

		Assertions.assertNotNull(func);
		ServiceHelper.withServiceConsumer(IAnalyticsScenarioEvaluator.class, func);
	}

}
