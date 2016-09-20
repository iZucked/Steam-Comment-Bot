/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

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
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.util.ScenarioInstanceSchedulingRule;

public abstract class AbstractLNGRunMultipleForkedJobsControl extends AbstractEclipseJobControl {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractLNGRunMultipleForkedJobsControl.class);

	private final AbstractLNGJobDescriptor jobDescriptor;

	private final ScenarioInstance scenarioInstance;

	private final ModelReference modelReference;

	private final LNGScenarioModel originalScenario;

	private static final ImageDescriptor imgOpti = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/elcl16/resume_co.gif");
	private static final ImageDescriptor imgEval = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/evaluate_schedule.gif");

	private final List<SimilarityFuture> jobs;
	private final ExecutorService controlService;
	private final ExecutorService runnerService;

	private class SimilarityFuture implements Runnable {

		private final LNGScenarioModel scenarioModel;
		private IProgressMonitor monitor;
		private final ScenarioInstance parent;
		private final String name;
		private final LNGScenarioRunner runner;
		private final ScenarioLock lock;
		private final ScenarioInstance fork;
		private final ModelReference ref;

		public SimilarityFuture(final ScenarioInstance parent, final LNGScenarioModel model, final String name, final OptimisationPlan optimisationPlan, final String... hints) {

			this.parent = parent;
			this.name = name;
			try {
				fork = createFork(model);
			} catch (IOException e) {
				throw new RuntimeException("Unable to create fork", e);
			}
			ref = fork.getReference("AbstractLNGRunMultipleForkedJobsControl:1");

			this.scenarioModel = (LNGScenarioModel) ref.getInstance();
			// TODO: This is probably a) null and b) bad idea to use the same executor service for sub-processes while the main process is using the pool....
			// TODO: Is is possible for a job to suspend itself and release the thread?

			this.runner = new LNGScenarioRunner(runnerService, scenarioModel, fork, optimisationPlan, (EditingDomain) fork.getAdapters().get(EditingDomain.class), null, false, hints);
			this.lock = fork.getLock(ScenarioLock.OPTIMISER);
			this.lock.awaitClaim();
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

	public AbstractLNGRunMultipleForkedJobsControl(final AbstractLNGJobDescriptor jobDescriptor, BiConsumer<OptimisationPlan, BiConsumer<String, OptimisationPlan>> jobFactory) throws IOException {
		super((jobDescriptor.isOptimising() ? "Optimise " : "Evaluate ") + jobDescriptor.getJobName(),
				CollectionsUtil.<QualifiedName, Object> makeHashMap(IProgressConstants.ICON_PROPERTY, (jobDescriptor.isOptimising() ? imgOpti : imgEval)));
		this.jobDescriptor = jobDescriptor;
		this.scenarioInstance = jobDescriptor.getJobContext();
		this.modelReference = scenarioInstance.getReference("AbstractLNGRunMultipleForkedJobsControl:2");
		this.originalScenario = (LNGScenarioModel) modelReference.getInstance();

		this.jobs = new LinkedList<>();
		BiConsumer<String, OptimisationPlan> factory = (name, optimisationPlan) -> {
			jobs.add(new SimilarityFuture(scenarioInstance, originalScenario, name, optimisationPlan, LNGTransformerHelper.HINT_OPTIMISE_LSO));
		};

		jobFactory.accept(jobDescriptor.getOptimisationPlan(), factory);

		// Hmm...
		final int numberOfThreads = getNumberOfThreads();
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
