/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm;

import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.trades.ITradesTableContextMenuExtension;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.OptimisationJobRunner;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class LightWeightSchedulerMenuExtension implements ITradesTableContextMenuExtension {

	private static final Logger log = LoggerFactory.getLogger(LightWeightSchedulerMenuExtension.class);

	public LightWeightSchedulerMenuExtension() {
	}

	@Override
	public void contributeToMenu(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final Slot slot, @NonNull final MenuManager menuManager) {
		if (!LicenseFeatures.isPermitted("features:light-weight-optimiser")) {
			return;
		}

		if (slot.getCargo() == null) {
			final LightWeightAction action = new LightWeightAction(scenarioEditingLocation);
			menuManager.add(action);
			return;
		}
	}

	private static class LightWeightAction extends Action {

		private final IScenarioEditingLocation scenarioEditingLocation;

		public LightWeightAction(final IScenarioEditingLocation scenarioEditingLocation) {
			super("Light weight scheduler");
			this.scenarioEditingLocation = scenarioEditingLocation;
		}

		@Override
		public void run() {
			final OptimisationJobRunner jobRunner = new OptimisationJobRunner();

			final ScenarioInstance instance = scenarioEditingLocation.getScenarioInstance();

			final @NonNull UserSettings userSettings = ScenarioUtils.createDefaultUserSettings();

			final Supplier<IJobDescriptor> createJobDescriptorCallback = () -> new LightWeightSchedulerJobDescriptor(instance.getName(), instance, userSettings);

			final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);
			jobRunner.run("LightWeight Scheduling", instance, modelRecord, null, createJobDescriptorCallback, null);
		}
	}

	@Override
	public void contributeToMenu(@NonNull IScenarioEditingLocation scenarioEditingLocation, @NonNull IStructuredSelection selection, @NonNull MenuManager menuManager) {

	}
}
