/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.sandbox;

import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
import com.mmxlabs.models.lng.analytics.util.SandboxModeConstants;
import com.mmxlabs.rcp.common.ServiceHelper;

public abstract class AbstractSandboxTestCase extends AbstractMicroTestCase {

	protected void evaluateSandbox(@NonNull OptionAnalysisModel model) {

		Consumer<IAnalyticsScenarioEvaluator> func = null;
		switch (model.getMode()) {
		case SandboxModeConstants.MODE_DERIVE:
			func = evaluator -> evaluator.runSandboxOptions(scenarioDataProvider, null, model, model::setResults, false);
			break;
		case SandboxModeConstants.MODE_OPTIMISE:
			func = evaluator -> evaluator.runSandboxOptimisation(scenarioDataProvider, null, model, model::setResults, false);
			break;
		case SandboxModeConstants.MODE_OPTIONISE:
			func = evaluator -> evaluator.runSandboxInsertion(scenarioDataProvider, null, model, model::setResults, false);
			break;
		}

		Assertions.assertNotNull(func);
		ServiceHelper.withServiceConsumer(IAnalyticsScenarioEvaluator.class, func);
	}

}
