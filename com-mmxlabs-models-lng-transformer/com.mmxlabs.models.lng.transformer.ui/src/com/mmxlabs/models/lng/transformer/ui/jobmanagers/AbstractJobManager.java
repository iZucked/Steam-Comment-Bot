/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobmanagers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.JobDataRecord;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.StringEscaper;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.IScenarioServiceListener;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.IProgressProvider;

public abstract class AbstractJobManager implements IJobManager, IProgressProvider {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractJobManager.class);

	protected final List<Task> localTasks = new LinkedList<>();

	protected IScenarioServiceListener scenarioServiceListener = new IScenarioServiceListener() {

		@Override
		public void onPreScenarioInstanceUnload(@NonNull IScenarioService scenarioService, @NonNull ScenarioInstance scenarioInstance) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPreScenarioInstanceDelete(@NonNull IScenarioService scenarioService, @NonNull ScenarioInstance scenarioInstance) {
			// TODO Auto-generated method stub

		}
	};

	public Collection<Task> getTasks() {
		synchronized (localTasks) {
			return new ArrayList<>(localTasks);
		}
	}

	public boolean hasActiveJob(final String scenarioUUID) {
		synchronized (localTasks) {
			for (final Task t : localTasks) {
				if (scenarioUUID.equals(t.job.getScenarioUUID())) {
					if (t.taskStatus.isSubmitted() || t.taskStatus.isRunning()) {
						return true;
					}
				}
			}
			return false;
		}
	}

	@Override
	public @Nullable Pair<Double, RunType> getProgress(final ScenarioInstance scenarioInstance, final @Nullable ScenarioFragment fragment) {
		final String scenarioUUID = scenarioInstance.getUuid();
		Task task = null;
		synchronized (localTasks) {
			for (final Task t : localTasks) {
				if (scenarioUUID.equals(t.job.getScenarioUUID())) {
					task = t;
				}
			}
		}

		if (task != null && task.taskStatus.isRunning()) {
			if (fragment == null //
					|| (fragment.getFragment() instanceof final UUIDObject uo //
							&& Objects.equals(uo.getUuid(), task.job.getComponentUUID()))) {

				return Pair.of(task.taskStatus.getProgress(), task.runType);
			}
		}
		return null;
	}

	protected final ConcurrentLinkedQueue<IProgressChanged> progressListeners = new ConcurrentLinkedQueue<>();

	@Override
	public void removeChangedListener(final IProgressChanged listener) {
		progressListeners.remove(listener);
	}

	@Override
	public void addChangedListener(final IProgressChanged listener) {
		progressListeners.add(listener);
	}

	protected void fireListeners(@Nullable final Task task) {
		final JobDataRecord jobDataRecord = task == null ? null : task.job;

		try {
			RunnerHelper.asyncExec(() -> progressListeners.forEach(p -> p.changed(task)));

			final ScenarioInstance instanceRef = jobDataRecord == null ? null : jobDataRecord.getScenarioInstance();
			if (instanceRef != null) {
				// Task and/or instance?
				RunnerHelper.asyncExec(() -> progressListeners.forEach(p -> p.changed(instanceRef)));

			}
		} catch (final Exception e) {
			LOG.error(e.getMessage(), e);
		}

	}

	protected void fireListeners() {
		try {
			RunnerHelper.asyncExec(() -> progressListeners.forEach(IProgressChanged::listChanged));
		} catch (final Exception e) {
			LOG.error(e.getMessage(), e);
		}

	}

	public synchronized void updateTaskStatus(final Task task, final TaskStatus newStatus) {

		if (task.taskStatus.isComplete() || task.taskStatus.isFailed()) {
			// Do not update further
		} else {
			task.taskStatus = newStatus;
			fireListeners(task);
		}

	}

	protected void handleError(final Task task, final String msg, Throwable ex) {

		// boolean visualStudioError = false;
		if (ex instanceof final NoClassDefFoundError noClassDefFoundError) {
			if (noClassDefFoundError.getMessage().contains("ortools")) {
				ex = new RuntimeException("Microsoft Visual C++ Redistributable 2019 not found. Please install the latest version from the Microsoft website.");
			}
		}

		LOG.error(msg, ex);
		if (task != null) {
			updateTaskStatus(task, TaskStatus.failed("Failed, see Error Log for more details"));
		}

		final boolean useDialogs = System.getProperty("lingo.suppress.dialogs") == null;

		if (useDialogs) {
			final Display display = Display.getDefault();
			if (display != null) {
				final Throwable pEx = ex;
				display.asyncExec(() -> {
					final String message = StringEscaper.escapeUIString(pEx.getMessage());
					MessageDialog.openError(display.getActiveShell(), "Error", "An error occured. See Error Log for more details.\n" + message);
				});
			}
		}
	}

}
