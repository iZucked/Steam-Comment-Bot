/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.menus;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.CloudJobManager;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsPushToCloudRunner;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox.SandboxTask;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class PushSandboxToCloudRunner implements IAnalyticsPushToCloudRunner {

	@Override
	public void run(@NonNull ScenarioInstance scenarioInstance, @NonNull OptionAnalysisModel sandboxModel) {
		SandboxTask.submit(scenarioInstance, sandboxModel, CloudJobManager.INSTANCE);
	}
}
