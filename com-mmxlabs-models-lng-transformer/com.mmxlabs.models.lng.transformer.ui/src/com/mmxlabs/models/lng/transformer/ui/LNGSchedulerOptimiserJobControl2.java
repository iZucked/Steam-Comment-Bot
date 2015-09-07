/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import org.apache.shiro.SecurityUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.IProgressConstants;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.jobmanager.eclipse.jobs.impl.AbstractEclipseJobControl2;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.util.ScenarioInstanceSchedulingRule;

public class LNGSchedulerOptimiserJobControl2 extends AbstractEclipseJobControl2 {

//	private static final int REPORT_PERCENTAGE = 1;
//	private int currentProgress = 0;

	private final LNGSchedulerJobDescriptor jobDescriptor;

	private final ScenarioInstance scenarioInstance;

	private final ModelReference modelReference;

	private final LNGScenarioModel originalScenario;

	private static final ImageDescriptor imgOpti = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/elcl16/resume_co.gif");
	private static final ImageDescriptor imgEval = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/evaluate_schedule.gif");

	private final LNGScenarioRunner scenarioRunner;

	public LNGSchedulerOptimiserJobControl2(final LNGSchedulerJobDescriptor jobDescriptor) {
		super((jobDescriptor.isOptimising() ? "Optimise " : "Evaluate ") + jobDescriptor.getJobName(),
				CollectionsUtil.<QualifiedName, Object> makeHashMap(IProgressConstants.ICON_PROPERTY, (jobDescriptor.isOptimising() ? imgOpti : imgEval)));
		this.jobDescriptor = jobDescriptor;
		this.scenarioInstance = jobDescriptor.getJobContext();
		this.modelReference = scenarioInstance.getReference();
		this.originalScenario = (LNGScenarioModel) modelReference.getInstance();
		final EditingDomain originalEditingDomain = (EditingDomain) scenarioInstance.getAdapters().get(EditingDomain.class);
		scenarioRunner = new LNGScenarioRunner(originalScenario, scenarioInstance, jobDescriptor.getOptimiserSettings(), originalEditingDomain, LNGTransformer.HINT_OPTIMISE_LSO);
		setRule(new ScenarioInstanceSchedulingRule(scenarioInstance));

		// Disable optimisation in P&L testing phase
		if (SecurityUtils.getSubject().isPermitted("features:phase-pnl-testing")) {
			throw new RuntimeException("Optimisation is disabled during the P&L testing phase.");
		}

	}

	@Override
	protected void reallyPrepare() {
		scenarioRunner.initAndEval();
	}

	@Override
	protected void doRunJob(IProgressMonitor progressMonitor) {

		progressMonitor.beginTask("Optimise", 100);
		try {
			// TODO Auto-generated method stub
			if (jobDescriptor.isOptimising() == false) {
				return; // if we are not optimising, finish.
			}
			scenarioRunner.runWithProgress(new SubProgressMonitor(progressMonitor, 100));
			super.setProgress(100);
		} finally {
			progressMonitor.done();
		}
		// if (scenarioRunner.isFinished()) {
		// return false;
		// } else {
		// return true;
		// }
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see com.mmxlabs.jobcontroller.core.AbstractManagedJob#step()
	// */
	// @Override
	// protected boolean step() {
	// if (jobDescriptor.isOptimising() == false) {
	// return false; // if we are not optimising, finish.
	// }
	// scenarioRunner.step(REPORT_PERCENTAGE);
	// currentProgress += REPORT_PERCENTAGE;
	//
	// super.setProgress(currentProgress);
	// if (scenarioRunner.isFinished()) {
	// super.setProgress(100);
	// return false;
	// } else {
	// return true;
	// }
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.jobcontroller.core.AbstractManagedJob#kill()
	 */
	@Override
	protected void kill() {
	}

	@Override
	public void dispose() {
		if (scenarioRunner != null) {
			scenarioRunner.dispose();
		}
		super.dispose();
	}

	@Override
	public final MMXRootObject getJobOutput() {
		return originalScenario;
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
