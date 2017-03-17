/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.ui;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXResultRoot;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.IDefaultScenarioResultProvider;

public class LNGScenarioResultProvider implements IDefaultScenarioResultProvider {

	@Override
	public @Nullable MMXResultRoot getDefaultResult(final ScenarioInstance scenarioInstance) {
		try (ModelReference ref = scenarioInstance.getReference("LNGScenarioResultProvider")) {
			final Object instance = ref.getInstance();
			if (instance instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) instance;
				return ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			}
		}
		return null;
	}

}
