/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.IProgressConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.jobmanager.eclipse.jobs.impl.AbstractEclipseJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.util.ScenarioInstanceSchedulingRule;

public class LNGSchedulerRunAllSimilarityJobControl extends AbstractEclipseJobControl {

	private static final Logger LOG = LoggerFactory.getLogger(LNGSchedulerRunAllSimilarityJobControl.class);

	private final LNGRunAllSimilarityJobDescriptor jobDescriptor;

	private final ScenarioInstance scenarioInstance;

	private final ModelReference modelReference;

	private final LNGScenarioModel originalScenario;

	private static final ImageDescriptor imgOpti = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/elcl16/resume_co.gif");
	private static final ImageDescriptor imgEval = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/evaluate_schedule.gif");

	private final SimilarityFuture[] jobs;
	private final ExecutorService controlService;
	private final ExecutorService runnerService;

	private class SimilarityFuture implements Runnable {

		private final LNGScenarioModel scenarioModel;
		private OptimiserSettings settings;
		private IProgressMonitor monitor;
		private final ScenarioInstance parent;
		private final String name;
		private final LNGScenarioRunner runner;
		private final ScenarioLock lock;
		private final ScenarioInstance fork;
		private final ModelReference ref;

		public SimilarityFuture(final ScenarioInstance parent, final LNGScenarioModel model, final String name, final OptimiserSettings settings, final String... hints) throws IOException {

			this.parent = parent;
			this.name = name;
			fork = createFork(model);
			ref = fork.getReference();
			this.scenarioModel = (LNGScenarioModel) ref.getInstance();
			// TODO: This is probably a) null and b) bad idea to use the same executor service for sub-processes while the main process is using the pool....
			// TODO: Is is possible for a job to suspend itself and release the thread?

			this.runner = new LNGScenarioRunner(runnerService, scenarioModel, fork, settings, (EditingDomain) fork.getAdapters().get(EditingDomain.class), null, false, hints);
			this.lock = fork.getLock(ScenarioLock.OPTIMISER);
			this.lock.awaitClaim();
		}

		public void init() {
			{
				//
				runner.evaluateInitialState();
			}

		}

		public void setParentProgressMonitor(final IProgressMonitor monitor, final int progressSlice) {
			this.monitor = new SubProgressMonitor(monitor, progressSlice);
		}

		@Override
		public void run() {
			IProgressMonitor pMonitor = monitor;
			if (pMonitor == null) {
				pMonitor = new NullProgressMonitor();
			}
			runner.runWithProgress(pMonitor);
		}

		public ScenarioInstance createFork(final LNGScenarioModel model) throws IOException {
			final IScenarioService scenarioService = parent.getScenarioService();
			final ScenarioInstance fork = scenarioService.insert(parent, EcoreUtil.copy(model));
			fork.setName(this.name);

			// Copy across various bits of information
			fork.getMetadata().setContentType(parent.getMetadata().getContentType());
			fork.getMetadata().setCreated(new Date());
			fork.getMetadata().setLastModified(new Date());

			// Copy version context information
			fork.setVersionContext(parent.getVersionContext());
			fork.setScenarioVersion(parent.getScenarioVersion());

			fork.setClientVersionContext(parent.getClientVersionContext());
			fork.setClientScenarioVersion(parent.getClientScenarioVersion());

			return fork;
		}

		public void dispose() {
			if (lock != null) {
				lock.release();
			}
			if (ref != null) {
				ref.close();
			}
		}
	}

	public LNGSchedulerRunAllSimilarityJobControl(final LNGRunAllSimilarityJobDescriptor jobDescriptor) throws IOException {
		super((jobDescriptor.isOptimising() ? "Optimise " : "Evaluate ") + jobDescriptor.getJobName(),
				CollectionsUtil.<QualifiedName, Object> makeHashMap(IProgressConstants.ICON_PROPERTY, (jobDescriptor.isOptimising() ? imgOpti : imgEval)));
		this.jobDescriptor = jobDescriptor;
		this.scenarioInstance = jobDescriptor.getJobContext();
		this.modelReference = scenarioInstance.getReference();
		this.originalScenario = (LNGScenarioModel) modelReference.getInstance();
		// final EditingDomain originalEditingDomain = (EditingDomain) scenarioInstance.getAdapters().get(EditingDomain.class);

		this.jobs = new SimilarityFuture[4];
		for (int i = 0; i < jobs.length; ++i) {
			final OptimiserSettings optimiserSettings = EcoreUtil.copy(jobDescriptor.getOptimiserSettings());
			String name;
			switch (i) {
			case 0:
				optimiserSettings.setSimilaritySettings(ScenarioUtils.createOffSimilaritySettings());
				optimiserSettings.setBuildActionSets(false);
				optimiserSettings.getAnnealingSettings().setRestarting(false);
				name = "Sim. Off";
				break;
			case 1:
				optimiserSettings.setSimilaritySettings(ScenarioUtils.createLowSimilaritySettings());
				optimiserSettings.getAnnealingSettings().setRestarting(false);
				name = "Sim. Low";
				break;
			case 2:
				optimiserSettings.setSimilaritySettings(ScenarioUtils.createMediumSimilaritySettings());
				optimiserSettings.getAnnealingSettings().setRestarting(false);
				name = "Sim. Medium";
				break;
			case 3:
				optimiserSettings.setSimilaritySettings(ScenarioUtils.createHighSimilaritySettings());
				name = "Sim. High";
				break;
			default:
				throw new IllegalStateException();
			}

			this.jobs[i] = new SimilarityFuture(scenarioInstance, originalScenario, name, optimiserSettings, LNGTransformerHelper.HINT_OPTIMISE_LSO);
		}
		// Hmm...
		final int numberOfThreads = Runtime.getRuntime().availableProcessors() - 1;
		setRule(new ScenarioInstanceSchedulingRule(scenarioInstance));
		// This executor is for the futures we create and execute here...
		controlService = Executors.newFixedThreadPool(numberOfThreads);
		// .. this executor is for the optimisation itself to avoid blocking the control executor
		runnerService = LNGScenarioChainBuilder.createExecutorService();
		// Disable optimisation in P&L testing phase
		if (LicenseFeatures.isPermitted("features:phase-pnl-testing")) {
			throw new RuntimeException("Optimisation is disabled during the P&L testing phase.");
		}

	}

	@Override
	protected void reallyPrepare() {

		for (final SimilarityFuture job : jobs) {
			job.init();
		}

	}

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
		if (!runnerService.isShutdown()) {
			runnerService.shutdownNow();
		}
		if (!controlService.isShutdown()) {
			controlService.shutdownNow();
		}
		// Clean up
		for (final SimilarityFuture job : jobs) {
			try {
				job.dispose();
			} catch (final Exception e) {
				LOG.error(e.getMessage(), e);
			}
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

	@Override
	protected void doRunJob(final IProgressMonitor progressMonitor) {

		final long startTime = System.currentTimeMillis();
		System.out.println(startTime);
		try {
			progressMonitor.beginTask("Optimise", 400);
			for (final SimilarityFuture job : jobs) {
				job.setParentProgressMonitor(progressMonitor, 100);
				controlService.submit(job);
			}
			// Block until jobs completed
			controlService.shutdown();
			try {
				while (!controlService.awaitTermination(100, TimeUnit.MILLISECONDS))
					;
			} catch (final InterruptedException e) {
				LOG.error(e.getMessage(), e);
			}
		} finally {
			progressMonitor.done();
			if (!runnerService.isShutdown()) {
				runnerService.shutdownNow();
			}
			if (!controlService.isShutdown()) {
				controlService.shutdownNow();
			}
			// Clean up
			for (final SimilarityFuture job : jobs) {
				try {
					job.dispose();
				} catch (final Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}

		long endTime = System.currentTimeMillis();
		System.out.println(endTime);
		System.out.println("Total Time " + (endTime - startTime) / 1000L);
	}
}
