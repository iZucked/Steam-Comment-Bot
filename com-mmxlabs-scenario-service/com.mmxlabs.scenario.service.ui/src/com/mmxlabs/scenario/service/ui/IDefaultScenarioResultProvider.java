/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.mmxcore.MMXResultRoot;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public interface IDefaultScenarioResultProvider {
	@Nullable
	MMXResultRoot getDefaultResult(ScenarioInstance instance);
}
