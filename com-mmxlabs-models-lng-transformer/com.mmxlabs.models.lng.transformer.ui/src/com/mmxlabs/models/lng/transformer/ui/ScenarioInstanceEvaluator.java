/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.transformer.ui.jobrunners.evaluate.EvaluateTask;
import com.mmxlabs.scenario.service.model.manager.IScenarioInstanceEvaluator;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class ScenarioInstanceEvaluator implements IScenarioInstanceEvaluator {
	@Override
	public void evaluate(final @NonNull ScenarioModelRecord modelRecord) {
		EvaluateTask.submit(modelRecord, false);
	}
}
