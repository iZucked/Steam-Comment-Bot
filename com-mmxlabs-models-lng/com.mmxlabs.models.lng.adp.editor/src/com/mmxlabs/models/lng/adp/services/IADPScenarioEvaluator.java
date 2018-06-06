/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.services;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPModelResult;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public interface IADPScenarioEvaluator {

	ADPModelResult runADPModel(@Nullable ScenarioInstance scenarioInstance, //
			@NonNull IScenarioDataProvider scenarioDataProvider, //
			@NonNull ADPModel adpModel, //
			@NonNull IProgressMonitor progressMonitor);

	ScenarioInstance export(ScenarioResult scenarioResult, String name) throws Exception;
}
