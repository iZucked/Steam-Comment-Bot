/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobmanagers;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.mmxlabs.common.util.CheckedFunction;
import com.mmxlabs.common.util.ToBooleanFunction;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.transformer.jobs.IJobRunner;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.JobDataRecord;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.JobRegistry;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelReferenceThread;
import com.mmxlabs.scenario.service.ui.IProgressProvider;

/**
 * A job managers for running tasks on the local machine. It will only allow a limited number to run concurrently.
 * 
 * @author sg
 *
 */
public class LocalJobManager extends AbstractJobManager {

	public static final LocalJobManager INSTANCE = new LocalJobManager();

	private LocalJobManager() {
		final Bundle bundle = FrameworkUtil.getBundle(LocalJobManager.class);
		final BundleContext bundleContext = bundle.getBundleContext();

		// Self register progress provider for UI
		bundleContext.registerService(IProgressProvider.class, this, null);
		// Self register as job manager service
		bundleContext.registerService(IJobManager.class, this, null);
	}

	private final Map<String, LocalJobProgressMonitor> localMonitors = new ConcurrentHashMap<>();

	/**
	 * Internal job manager. Defines the max concurrent local jobs
	 */
	private final ExecutorService executorService = Executors.newFixedThreadPool(2);

	@Override
	public void cancel(final Task task) {

		final LocalJobProgressMonitor monitor = localMonitors.get(task.job.getScenarioUUID());
		if (monitor != null) {
			monitor.setCanceled(true);
		}
	}

	@Override
	public void cancelAll(final String scenarioUUID) {

		final LocalJobProgressMonitor monitor = localMonitors.get(scenarioUUID);
		if (monitor != null) {
			monitor.setCanceled(true);
		}
	}

	@Override
	public void remove(final @NonNull Task task) {
		synchronized (localTasks) {
			final LocalJobProgressMonitor monitor = localMonitors.remove(task.job.getScenarioUUID());
			if (monitor != null) {
				monitor.setCanceled(true);
			}
			localTasks.remove(task);
		}
		// Task removed
		fireListeners();
	}

	@Override
	public void removeAll(final String scenarioUUID) {
		synchronized (localTasks) {
			final LocalJobProgressMonitor monitor = localMonitors.remove(scenarioUUID);
			if (monitor != null) {
				monitor.setCanceled(true);
			}

			final Iterator<Task> itr = localTasks.iterator();
			while (itr.hasNext()) {
				final Task t = itr.next();
				if (Objects.equals(scenarioUUID, t.job.getScenarioUUID())) {
					itr.remove();
				}
			}
		}
		// Task removed
		fireListeners();
	}

	@Override
	public void submit(final String taskName, final JobDataRecord job, final CheckedFunction<IScenarioDataProvider, @Nullable String, Exception> parametersFactory,
			final ToBooleanFunction<IScenarioDataProvider> validationFactory, final BiConsumer<IScenarioDataProvider, AbstractSolutionSet> applyFunction) {

		final ScenarioModelRecord scenarioModelRecord = SSDataManager.Instance.getModelRecordChecked(job.getScenarioInstance());
		// We need a special unique thread for exclusive locking.
		final ScenarioModelReferenceThread thread = new ScenarioModelReferenceThread(taskName, scenarioModelRecord, sdp -> {
			final ScenarioLock scenarioLock = sdp.getModelReference().getLock();
			scenarioLock.withTryLock(2_000, () -> {
				try {

					final String paramsJSON = parametersFactory.apply(sdp);
					if (paramsJSON == null) {
						return;
					}

					if (!validationFactory.accept(sdp)) {
						return;
					}

					job.setTaskParameters(paramsJSON);

					final Task task = new Task();
					task.runType = RunType.Local;
					task.job = job;
					task.modelRecord = scenarioModelRecord;
					task.sdp = sdp;

					task.successHandler = applyFunction;
					task.errorHandler = (a, b) -> handleError(task, a, b);

					final LocalJobProgressMonitor monitor = new LocalJobProgressMonitor(task);
					synchronized (localTasks) {
						localTasks.add(task);
						this.localMonitors.put(job.getScenarioUUID(), monitor);
					}

					final Future<?> taskRef = executorService.submit(() -> executeTask(task, monitor));
					// New task added
					fireListeners();
					// Initial progress update
					fireListeners(task);

					// Block until completed.
					taskRef.get();

				} catch (final Throwable ex) {
					handleError(null, ex.getMessage(), ex);
				}
			});
		});

		thread.start();

	}

	private void executeTask(final @NonNull Task task, final @NonNull LocalJobProgressMonitor monitor) {

		final IJobRunner runner = JobRegistry.INSTANCE.newRunner(task.job.getType());

		try {
			runner.withParams(task.job.getTaskParameters());
		} catch (final IOException e) {
			task.errorHandler.accept("Error processing task parameters", e);
		}
		runner.withScenario(task.sdp);
		runner.withScenarioInstance(task.job.getScenarioInstance());

		try {
			final AbstractSolutionSet result = runner.run(monitor);
			if (result != null) {
				task.job.setResultUUID(result.getUuid());
			}
			task.successHandler.accept(task.sdp, result);
		} catch (final Exception e) {
			task.errorHandler.accept("Error running task", e);
			updateTaskStatus(task, TaskStatus.failed());
		} finally {
			monitor.done();
		}
	}

	class LocalJobProgressMonitor implements IProgressMonitor {
		Task task;
		private double lastReport = -1;
		private double dtotal = 0.0;
		private int totalWork;
		private boolean canceled = false;

		LocalJobProgressMonitor(final Task task) {
			this.task = task;
		}

		@Override
		public synchronized void worked(final int work) {

			final double w = (double) work / (double) totalWork;
			internalWorked(w);
		}

		@Override
		public void subTask(final String name) {

		}

		@Override
		public void setTaskName(final String name) {

		}

		@Override
		public synchronized void setCanceled(final boolean canceled) {
			this.canceled = canceled;
			updateTaskStatus(task, TaskStatus.aborted());
			fireListeners(task);
		}

		@Override
		public synchronized boolean isCanceled() {
			return canceled;
		}

		@Override
		public synchronized void internalWorked(final double work) {
			if (!canceled) {
				dtotal += work;
				System.out.printf("Progress:%.2f%n", 100.0 * dtotal);

				if (dtotal > lastReport + 0.01) {
					updateTaskStatus(task, TaskStatus.running(dtotal));
					lastReport = dtotal;
				}
			}
		}

		@Override
		public synchronized void done() {
			updateTaskStatus(task, TaskStatus.complete());
		}

		public synchronized double getProgress() {
			return dtotal;
		}

		@Override
		public synchronized void beginTask(final String name, final int totalWork) {
			this.totalWork = totalWork;
			updateTaskStatus(task, TaskStatus.running(0.0));

		}
	}
}
