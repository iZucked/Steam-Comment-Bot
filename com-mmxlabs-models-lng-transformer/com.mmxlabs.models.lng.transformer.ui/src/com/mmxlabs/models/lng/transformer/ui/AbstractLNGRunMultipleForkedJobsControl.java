/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.IProgressConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.concurrent.CleanableExecutorService;
import com.mmxlabs.common.util.CheckedBiConsumer;
import com.mmxlabs.jobmanager.eclipse.jobs.impl.AbstractEclipseJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;
import com.mmxlabs.scenario.service.util.ScenarioInstanceSchedulingRule;

public abstract class AbstractLNGRunMultipleForkedJobsControl extends AbstractEclipseJobControl {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractLNGRunMultipleForkedJobsControl.class);

	private final AbstractLNGJobDescriptor jobDescriptor;

	private final ScenarioInstance scenarioInstance;

	private final ScenarioModelRecord modelRecord;
	private final ModelReference modelReference;

	private final LNGScenarioModel originalScenario;

	private static final ImageDescriptor imgOpti = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/elcl16/resume_co.gif");
	private static final ImageDescriptor imgEval = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/evaluate_schedule.gif");

	private final List<SimilarityFuture> jobs;
	private final CleanableExecutorService controlService;

	private class SimilarityFuture implements Runnable {

		private final IScenarioDataProvider forkScenarioDataProvider;
		private IProgressMonitor monitor;
		private final ScenarioInstance parent;
		private final String name;
		private final LNGScenarioRunner runner;
		private final ScenarioLock lock;
		private final ScenarioInstance fork;
		private final ScenarioModelRecord forkRecord;

		public SimilarityFuture(final ScenarioInstance parent, final String name, final OptimisationPlan optimisationPlan, final String... hints) throws Exception {

			this.parent = parent;
			this.name = name;
			fork = ScenarioServiceModelUtils.fork(parent, name, new NullProgressMonitor());
			forkRecord = SSDataManager.Instance.getModelRecord(fork);
			this.forkScenarioDataProvider = forkRecord.aquireScenarioDataProvider("AbstractLNGRunMultipleForkedJobsControl:1");
			this.runner = LNGOptimisationBuilder.begin(forkScenarioDataProvider, fork) //
					.withOptimisationPlan(optimisationPlan) //
					.withOptimiseHint() //
					.withHints(hints) //
					.buildDefaultRunner().getScenarioRunner();
			this.lock = forkScenarioDataProvider.getModelReference().getLock();
			this.lock.lock();
		}

		public void init() {
			runner.evaluateInitialState();
		}

		public void setParentProgressMonitor(final IProgressMonitor monitor, final int progressSlice) {
			this.monitor = new SubProgressMonitor(monitor, progressSlice);
		}

		@Override
		public void run() {
			try {
				IProgressMonitor pMonitor = monitor;
				if (pMonitor == null) {
					pMonitor = new NullProgressMonitor();
				}
				runner.runWithProgress(pMonitor);
			} finally {
				dispose();
			}
		}

		public void dispose() {
			if (lock != null) {
				lock.unlock();
			}
			if (runner != null) {
				runner.getExecutorService().shutdownNow();
			}
			if (forkScenarioDataProvider != null) {
				forkScenarioDataProvider.close();
			}
		}
	}

	public AbstractLNGRunMultipleForkedJobsControl(final AbstractLNGJobDescriptor jobDescriptor,
			CheckedBiConsumer<OptimisationPlan, CheckedBiConsumer<String, OptimisationPlan, Exception>, Exception> jobFactory) throws Exception {
		super((jobDescriptor.isOptimising() ? "Optimise " : "Evaluate ") + jobDescriptor.getJobName(),
				CollectionsUtil.<QualifiedName, Object> makeHashMap(IProgressConstants.ICON_PROPERTY, (jobDescriptor.isOptimising() ? imgOpti : imgEval)));
		this.jobDescriptor = jobDescriptor;
		this.scenarioInstance = jobDescriptor.getJobContext();
		this.modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
		this.modelReference = modelRecord.aquireReference("AbstractLNGRunMultipleForkedJobsControl:2");
		this.originalScenario = (LNGScenarioModel) modelReference.getInstance();

		this.jobs = new LinkedList<>();
		CheckedBiConsumer<String, OptimisationPlan, Exception> factory = (name, optimisationPlan) -> {
			jobs.add(new SimilarityFuture(scenarioInstance, name, optimisationPlan, LNGTransformerHelper.HINT_OPTIMISE_LSO));
		};

		jobFactory.accept(jobDescriptor.getOptimisationPlan(), factory);

		// Hmm...
		final int numberOfThreads = getNumberOfThreads();
		setRule(new ScenarioInstanceSchedulingRule(scenarioInstance));
		// This executor is for the futures we create and execute here...
		controlService = LNGScenarioChainBuilder.createExecutorService(numberOfThreads);
		// Disable optimisation in P&L testing phase
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_PHASE_PNL_TESTING) || LicenseFeatures.isPermitted(KnownFeatures.FEATURE_PHASE_LIMITED_TESTING)) {
			throw new RuntimeException("Optimisation is disabled during the P&L testing phase.");
		}
	}

	/**
	 * Returns the number of theads to use.
	 * 
	 * @return
	 */
	protected int getNumberOfThreads() {
		return Math.max(1, Runtime.getRuntime().availableProcessors() - 2);
	}

	@Override
	protected void reallyPrepare() {

		for (final SimilarityFuture job : jobs) {
			job.init();
		}

	}

	@Override
	protected void kill() {

	}

	@Override
	public void dispose() {
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
		jobs.clear();

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
			progressMonitor.beginTask("Optimise", jobs.size() * 100);
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
