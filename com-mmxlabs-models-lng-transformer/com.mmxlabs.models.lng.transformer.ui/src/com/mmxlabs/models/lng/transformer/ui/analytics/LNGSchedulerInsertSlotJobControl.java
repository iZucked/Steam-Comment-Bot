/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.util.List;
import java.util.concurrent.ExecutorService;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.jobmanager.eclipse.jobs.impl.AbstractEclipseJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.util.ScenarioInstanceSchedulingRule;

public class LNGSchedulerInsertSlotJobControl extends AbstractEclipseJobControl {

	private final LNGSlotInsertionJobDescriptor jobDescriptor;

	private final LNGSchedulerInsertSlotJobRunner runner;
	private final ModelReference modelReference;	
	private final ExecutorService executorService;
	private SlotInsertionOptions slotInsertionPlan;

	public LNGSchedulerInsertSlotJobControl(final LNGSlotInsertionJobDescriptor jobDescriptor) {
		super(jobDescriptor.getJobName());

		this.jobDescriptor = jobDescriptor;

		final ScenarioInstance scenarioInstance = jobDescriptor.getJobContext();
		assert scenarioInstance != null;
		final ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
		this.modelReference = modelRecord.aquireReference("LNGSchedulerInsertSlotJobControl");
		EditingDomain editingDomain = modelReference.getEditingDomain();
		LNGScenarioModel originalScenario = (LNGScenarioModel) modelReference.getInstance();

		/*
		 * Error checks
		 */
		{
			// Disable optimisation in P&L testing phase
			if (LicenseFeatures.isPermitted("features:phase-pnl-testing")) {
				throw new RuntimeException("Optimisation is disabled during the P&L testing phase.");
			}
			// if (!OptimisationHelper.isAllowedGCO(this.originalScenario)) {
			// throw new RuntimeException("Optimisation is disabled when missing prices are used");
			// }
		}

		// TODO: This should be static / central service?
		executorService = LNGScenarioChainBuilder.createExecutorService();

		final List<Slot> targetSlots = jobDescriptor.getTargetSlots();
		final List<VesselEvent> targetEvents = jobDescriptor.getTargetEvents();
		final UserSettings userSettings = jobDescriptor.getUserSettings();

		runner = new LNGSchedulerInsertSlotJobRunner(executorService, scenarioInstance, originalScenario, editingDomain, userSettings, targetSlots, targetEvents);

		setRule(new ScenarioInstanceSchedulingRule(scenarioInstance));
	}

	@Override
	protected void reallyPrepare() {
		runner.prepare();
	}

	@Override
	protected void doRunJob(final IProgressMonitor progressMonitor) {
		this.slotInsertionPlan = runner.doRunJob(progressMonitor);
	}

	@Override
	protected void kill() {
	}

	@Override
	public void dispose() {
		executorService.shutdownNow();

		if (modelReference != null) {
			modelReference.close();
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
