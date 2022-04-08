/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.liveeval.internal;

import java.util.concurrent.ForkJoinPool;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.preference.IPreferenceStore;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.manager.ILiveEvaluatorService;
import com.mmxlabs.scenario.service.model.manager.IPostChangeHook;
import com.mmxlabs.scenario.service.model.manager.IScenarioInstanceEvaluator;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class LiveEvaluatorServiceListener implements ILiveEvaluatorService, IPostChangeHook {

	private boolean enabled = true;

	private IScenarioInstanceEvaluator scenarioInstanceEvaluator;

	@Override
	public void setLiveEvaluatorEnabled(final boolean enabled) {
		this.enabled = enabled;

		final IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		preferenceStore.setValue("LiveEvaluatorEnabled", enabled);
	}

	@Override
	public boolean isLiveEvaluatorEnabled() {
		return enabled;
	}

	public IScenarioInstanceEvaluator getScenarioInstanceEvaluator() {
		return scenarioInstanceEvaluator;
	}

	public void setScenarioInstanceEvaluator(final IScenarioInstanceEvaluator scenarioInstanceEvaluator) {
		this.scenarioInstanceEvaluator = scenarioInstanceEvaluator;
	}

	@Override
	public void changed(@NonNull final ScenarioModelRecord modelRecord) {
		if (!enabled) {
			return;
		}
		// Do not bother to evaluate if there is an error.
		if (modelRecord.getValidationStatusSeverity() == IStatus.ERROR) {
			return;
		}

		ForkJoinPool.commonPool().submit(() -> {
			try (ModelReference ref = modelRecord.aquireReferenceIfLoaded("LiveEvaluatorServiceListener")) {
				if (ref == null) {
					return;
				}
				final LNGScenarioModel root = (LNGScenarioModel) ref.getInstance();
				final ScheduleModel scheduleModel = root.getScheduleModel();
				if (!scheduleModel.isDirty()) {
					return;
				}
				// Run in display thread as the IScenarioInstanceEvaluator should also execute
				// changes with the display thread using syncExec. This can cause deadlock if
				// some other process has acquired
				// the display thread before the read/write lock.
				RunnerHelper.syncExec(() -> ref.executeWithTryLock(false, () -> {

					// Do not try to re-evaluate failed evaluation
					if (ref.isLastEvaluationFailed()) {
						return;
					}

					// Re-check dirty status in lock
					if (!scheduleModel.isDirty()) {
						return;
					}
					// Do not bother to evaluate if there is an error.
					if (modelRecord.getValidationStatusSeverity() == IStatus.ERROR) {
						return;
					}
					scenarioInstanceEvaluator.evaluate(modelRecord);
				}));
			}
		});
	}

	public void start() {

		final IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		setLiveEvaluatorEnabled(preferenceStore.getBoolean("LiveEvaluatorEnabled"));

		SSDataManager.Instance.registerChangeHook(this, SSDataManager.PostChangeHookPhase.EVALUATION);
	}

	public void stop() {
		SSDataManager.Instance.removeChangeHook(this, SSDataManager.PostChangeHookPhase.EVALUATION);
	}
}
