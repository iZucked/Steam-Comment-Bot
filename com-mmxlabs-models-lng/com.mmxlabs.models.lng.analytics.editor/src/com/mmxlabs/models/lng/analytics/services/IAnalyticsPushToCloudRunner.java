/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.services;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public interface IAnalyticsPushToCloudRunner {
	public void run(@NonNull final ScenarioInstance scenarioInstance, @NonNull final OptionAnalysisModel sandboxModel);
}
