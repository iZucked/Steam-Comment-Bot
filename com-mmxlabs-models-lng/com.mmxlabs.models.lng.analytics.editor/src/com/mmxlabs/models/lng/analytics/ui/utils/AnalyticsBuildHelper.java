/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.utils;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public class AnalyticsBuildHelper {

	private AnalyticsBuildHelper() {
	}

	public static UserSettings getSandboxPreviousSettings(final OptionAnalysisModel model, @NonNull final LNGScenarioModel root) {
		UserSettings previousSettings = null;
		if (model.getResults() != null) {
			previousSettings = model.getResults().getUserSettings();
		}
		if (previousSettings == null) {
			previousSettings = root.getUserSettings();
		}
		return previousSettings;
	}
}
