/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.menus;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsPushToCloudRunner;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class PushSandboxToCloudRunner implements IAnalyticsPushToCloudRunner {

	@Override
	public void run(@NonNull ScenarioInstance scenarioInstance, @NonNull OptionAnalysisModel sandboxModel) {
		BusyIndicator.showWhile(Display.getDefault(), () -> {
			ScenarioServicePushToCloudAction.uploadScenario(scenarioInstance, sandboxModel);
		});
	}

}
