/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.internal;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IPostChangeHook;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class ReadOnlyScenarioHook implements IPostChangeHook {
	@Override
	public void changed(@NonNull final ScenarioModelRecord modelRecord) {

		if (true) {
			return;
		}

		try (ModelReference ref = modelRecord.aquireReferenceIfLoaded("ReadOnlyScenarioHandler:1")) {
			if (ref != null) {
				@NonNull
				final EObject instance = ref.getInstance();
				if (instance instanceof LNGScenarioModel) {
					final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) instance;
					final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(lngScenarioModel);
					if (analyticsModel == null) {
						return;
					}
					final boolean makeReadOnly =false // !analyticsModel.getInsertionOptions().isEmpty() //
							//|| !analyticsModel.getActionableSetPlans().isEmpty() //
					;

					if (makeReadOnly) {
						modelRecord.setReadOnly(makeReadOnly);

						final @Nullable ScenarioInstance scenarioInstance = modelRecord.getScenarioInstance();
						if (scenarioInstance != null) {
							RunnerHelper.syncExecDisplayOptional(() -> scenarioInstance.setReadonly(true));
						}
					}
				}
			}
		}

	}

	public void start() {
		SSDataManager.Instance.registerChangeHook(this, SSDataManager.PostChangeHookPhase.ON_LOAD);
	}

	public void stop() {
		SSDataManager.Instance.removeChangeHook(this, SSDataManager.PostChangeHookPhase.ON_LOAD);
	}
}
