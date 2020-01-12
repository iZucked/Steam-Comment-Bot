/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.jobmanager.eclipse.jobs.impl.AbstractEclipseJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.util.ScenarioInstanceSchedulingRule;

public class LNGSchedulerInsertSlotJobControl extends AbstractEclipseJobControl {

	private final LNGSlotInsertionJobDescriptor jobDescriptor;
	private final IScenarioDataProvider scenarioDataProvider;

	private final LNGSchedulerInsertSlotJobRunner runner;
	private SlotInsertionOptions slotInsertionPlan;
	private String taskName;

	public LNGSchedulerInsertSlotJobControl(final LNGSlotInsertionJobDescriptor jobDescriptor) {
		super(jobDescriptor.getJobName());

		this.jobDescriptor = jobDescriptor;

		final ScenarioInstance scenarioInstance = jobDescriptor.getJobContext();
		assert scenarioInstance != null;
		final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
		this.scenarioDataProvider = modelRecord.aquireScenarioDataProvider("LNGSchedulerInsertSlotJobControl");
		final EditingDomain editingDomain = scenarioDataProvider.getEditingDomain();

		/*
		 * Error checks
		 */
		{
			// Disable optimisation in P&L testing phase
			if (LicenseFeatures.isPermitted("features:phase-pnl-testing") || LicenseFeatures.isPermitted("features:phase-limited-testing")) {
				throw new RuntimeException("Optimisation is disabled during the P&L testing phase.");
			}
			// if (!OptimisationHelper.isAllowedGCO(this.originalScenario)) {
			// throw new RuntimeException("Optimisation is disabled when missing prices are used");
			// }
		}

		final List<Slot<?>> targetSlots = jobDescriptor.getTargetSlots();
		final List<VesselEvent> targetEvents = jobDescriptor.getTargetEvents();
		final UserSettings userSettings = jobDescriptor.getUserSettings();
		this.taskName = jobDescriptor.getJobName();
		runner = new LNGSchedulerInsertSlotJobRunner(scenarioInstance, scenarioDataProvider, editingDomain, userSettings, targetSlots, targetEvents, null, null);

		setRule(new ScenarioInstanceSchedulingRule(scenarioInstance));
	}

	@Override
	protected void reallyPrepare() {
		runner.prepare();
	}

	@Override
	protected void doRunJob(final IProgressMonitor progressMonitor) {
		this.slotInsertionPlan = runner.doRunJob(progressMonitor);
		if (this.slotInsertionPlan != null) {
			this.slotInsertionPlan.setName(taskName);
		}
	}

	@Override
	protected void kill() {
	}

	@Override
	public void dispose() {
		if (runner != null) {
			runner.dispose();
		}

		if (scenarioDataProvider != null) {
			scenarioDataProvider.close();
		}

		super.dispose();
	}

	@Override
	public final SlotInsertionOptions getJobOutput() {
		return slotInsertionPlan;
	}

	@Override
	public IJobDescriptor getJobDescriptor() {
		return jobDescriptor;
	}

	@Override
	public boolean isPauseable() {
		return true;
	}

}
