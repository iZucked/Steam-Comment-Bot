/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsBuildHelper;
import com.mmxlabs.models.lng.analytics.util.SandboxModeConstants;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.editor.util.UserSettingsHelper;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.rcp.common.ServiceHelper;

public abstract class AbstractSandboxTestCase extends AbstractMicroTestCase {

	protected void evaluateSandbox(@NonNull OptionAnalysisModel model) {
		final LNGScenarioModel root = ScenarioModelUtil.findScenarioModel(model);
		UserSettings previousSettings;
		UserSettings userSettings;
		assert root != null;
		Consumer<IAnalyticsScenarioEvaluator> func = null;
		switch (model.getMode()) {
		case SandboxModeConstants.MODE_DERIVE:
			previousSettings = AnalyticsBuildHelper.getSandboxPreviousSettings(model, root);
			userSettings = UserSettingsHelper.promptForSandboxUserSettings(root, false, false, false, null, previousSettings);
			func = evaluator -> evaluator.runSandboxOptions(scenarioDataProvider, null, model, userSettings, model::setResults, false, new NullProgressMonitor());
			break;
		case SandboxModeConstants.MODE_OPTIMISE:
			previousSettings = AnalyticsBuildHelper.getSandboxPreviousSettings(model, root);
			userSettings = UserSettingsHelper.promptForUserSettings(root, false, false, false, null, previousSettings);
			func = evaluator -> evaluator.runSandboxOptimisation(scenarioDataProvider, null, model, userSettings, model::setResults, false, new NullProgressMonitor());
			break;
		case SandboxModeConstants.MODE_OPTIONISE:
			previousSettings = AnalyticsBuildHelper.getSandboxPreviousSettings(model, root);
			userSettings = UserSettingsHelper.promptForInsertionUserSettings(root, false, false, false, null, previousSettings);
			func = evaluator -> evaluator.runSandboxInsertion(scenarioDataProvider, null, model, userSettings, model::setResults, false, new NullProgressMonitor());
			break;
		}

		Assertions.assertNotNull(func);
		ServiceHelper.withServiceConsumer(IAnalyticsScenarioEvaluator.class, func);
	}

}
