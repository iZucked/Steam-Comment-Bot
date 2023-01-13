/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.sandbox;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.parameters.editor.util.UserSettingsHelper;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox.SandboxJobRunner;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox.SandboxSettings;

public abstract class AbstractSandboxTestCase extends AbstractMicroTestCase {

	protected void evaluateSandbox(@NonNull OptionAnalysisModel model) {
		final LNGScenarioModel root = ScenarioModelUtil.findScenarioModel(model);
		assert root != null;

		final SandboxJobRunner runner = new SandboxJobRunner();
		runner.withScenario(scenarioDataProvider);

		final SandboxSettings settings = new SandboxSettings();
		settings.setSandboxUUID(model.getUuid());

		settings.setUserSettings(root.getUserSettings());
		if (settings.getUserSettings() == null) {
			settings.setUserSettings(UserSettingsHelper.createDefaultUserSettings());
		}
		runner.withParams(settings);

		AbstractSolutionSet result = runner.run(new NullProgressMonitor());
		model.setResults(result);
	}

}
