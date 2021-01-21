/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.mmxcore.MMXResultRoot;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public interface IDefaultScenarioResultProvider {
	@Nullable
	MMXResultRoot getDefaultResult(ScenarioModelRecord instance);
}
