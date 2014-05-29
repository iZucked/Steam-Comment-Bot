/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import javax.management.timer.Timer;

import org.apache.shiro.SecurityUtils;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.IProgressConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.jobmanager.eclipse.jobs.impl.AbstractEclipseJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.util.ScenarioInstanceSchedulingRule;

public class LNGSchedulerOptimiserJobControl extends AbstractEclipseJobControl {

	private static final Logger log = LoggerFactory.getLogger(LNGSchedulerOptimiserJobControl.class);

	private static final int REPORT_PERCENTAGE = 1;
	private int currentProgress = 0;

	private final LNGSchedulerJobDescriptor jobDescriptor;

	private final ScenarioInstance scenarioInstance;

	private final LNGScenarioModel scenario;

	private ModelEntityMap modelEntityMap;

	private LocalSearchOptimiser optimiser;

	private long startTimeMillis;

	private final EditingDomain editingDomain;

	private static final ImageDescriptor imgOpti = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/elcl16/resume_co.gif");
	private static final ImageDescriptor imgEval = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/evaluate_schedule.gif");

	private Injector injector = null;

	// private String lockKey;

	private LNGTransformer transformer;

	public LNGSchedulerOptimiserJobControl(final LNGSchedulerJobDescriptor jobDescriptor) {
		super((jobDescriptor.isOptimising() ? "Optimise " : "Evaluate ") + jobDescriptor.getJobName(), CollectionsUtil.<QualifiedName, Object> makeHashMap(IProgressConstants.ICON_PROPERTY,
				(jobDescriptor.isOptimising() ? imgOpti : imgEval)));
		this.jobDescriptor = jobDescriptor;
		this.scenarioInstance = jobDescriptor.getJobContext();
		this.scenario = (LNGScenarioModel) scenarioInstance.getInstance();
		editingDomain = (EditingDomain) scenarioInstance.getAdapters().get(EditingDomain.class);
		setRule(new ScenarioInstanceSchedulingRule(scenarioInstance));

		// Disable optimisation in P&L testing phase
		if (SecurityUtils.getSubject().isPermitted("features:phase-pnl-testing")) {
			// Note escaped &!
			throw new RuntimeException("Optimisation is disabled during the P&&L testing phase.");
		}

	}

	@Override
	protected void reallyPrepare() {
		startTimeMillis = System.currentTimeMillis();

		transformer = new LNGTransformer(scenario, jobDescriptor.getOptimiserSettings(), LNGTransformer.HINT_OPTIMISE_LSO);

		injector = transformer.getInjector();

		// final IOptimisationData data = transformer.getOptimisationData();
		modelEntityMap = transformer.getModelEntityMap();

		final IOptimisationContext context = transformer.getOptimisationContext();
		optimiser = transformer.getOptimiser();

		// because we are driving the optimiser ourself, so we can be paused, we
		// don't actually get progress callbacks.
		optimiser.setProgressMonitor(new NullOptimiserProgressMonitor());

		optimiser.init();
		final IAnnotatedSolution startSolution = optimiser.start(context);

		LNGSchedulerJobUtils.exportSolution(injector, scenario, transformer.getOptimiserSettings(), editingDomain, modelEntityMap, startSolution, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.jobcontroller.core.AbstractManagedJob#step()
	 */
	@Override
	protected boolean step() {
		// final ScheduleModel scheduleModel = scenario.getSubModel(ScheduleModel.class);
		if (jobDescriptor.isOptimising() == false) {
			// clear lock
			// scenarioInstance.getLock(lockKey).release();
			return false; // if we are not optimising, finish.
		}
		optimiser.step(REPORT_PERCENTAGE);
		currentProgress += REPORT_PERCENTAGE;

		super.setProgress(currentProgress);
		if (optimiser.isFinished()) {
			// export final state

			LNGSchedulerJobUtils.undoPreviousOptimsationStep(editingDomain, 100);
			LNGSchedulerJobUtils.exportSolution(injector, scenario, transformer.getOptimiserSettings(), editingDomain, modelEntityMap, optimiser.getBestSolution(true), 100);
			optimiser = null;
			log.debug(String.format("Job finished in %.2f minutes", (System.currentTimeMillis() - startTimeMillis) / (double) Timer.ONE_MINUTE));
			super.setProgress(100);
			// scenarioInstance.getLock(lockKey).release();
			return false;
		} else {
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.jobcontroller.core.AbstractManagedJob#kill()
	 */
	@Override
	protected void kill() {
		if (optimiser != null) {
			optimiser.dispose();
			optimiser = null;
		}
	}

	@Override
	public void dispose() {

		kill();
		if (this.modelEntityMap != null) {
			this.modelEntityMap.dispose();
			this.modelEntityMap = null;
		}

		// TODO: this.scenario = null;
		this.optimiser = null;

		super.dispose();
	}

	@Override
	public final MMXRootObject getJobOutput() {
		return scenario;
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
