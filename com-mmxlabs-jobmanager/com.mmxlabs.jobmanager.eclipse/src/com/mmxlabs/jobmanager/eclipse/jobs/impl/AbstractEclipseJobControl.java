/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.eclipse.jobs.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.util.exceptions.UserFeedbackException;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.rcp.common.RunnerHelper;

/**
 * An abstract class which provides most of what you want for any job.
 * 
 * Subclasses should only need to implement the missing methods.
 * 
 * @author hinton
 * 
 */
public abstract class AbstractEclipseJobControl implements IJobControl {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractEclipseJobControl.class);

	private class Runner extends Job {

		public Runner(final String name) {
			super(name);
		}

		@Override
		public IStatus run(final IProgressMonitor parentMonitor) {

			final BindingProgressMonitor monitor = new BindingProgressMonitor(AbstractEclipseJobControl.this, parentMonitor);

			monitor.beginTask("", 100);
			try {
				setJobState(EJobState.RUNNING);

				doRunJob(SubMonitor.convert(monitor, 100));
				if (monitor.isCanceled()) {
					setJobState(EJobState.CANCELLED);
					return Status.CANCEL_STATUS;
				}
				setProgress(100);
				setJobState(EJobState.COMPLETED);
				return Status.OK_STATUS;
			} catch (final OperationCanceledException e) {
				setJobState(EJobState.CANCELLED);
				return Status.CANCEL_STATUS;
			} catch (final UserFeedbackException e) {
				LOG.warn(e.getMessage());
				setJobState(EJobState.CANCELLED);

				if (System.getProperty("lingo.suppress.dialogs") == null) {
					RunnerHelper.asyncExec(display -> MessageDialog.openInformation(display.getActiveShell(), getName(), e.getMessage()));
				} else {
					throw e;
				}
				return Status.CANCEL_STATUS;

			} catch (final Exception | Error e) {

				boolean visualStudioError = false;
				if (e instanceof final NoClassDefFoundError noClassDefFoundError) {
					if (noClassDefFoundError.getMessage().contains("ortools")) {
						visualStudioError = true;
					}
				}

				setJobState(EJobState.CANCELLED);

				if (visualStudioError) {
					throw new RuntimeException("Microsoft Visual C++ Redistributable 2019 not found. Please install the latest version from the Microsoft website.");
				}
				LOG.error(e.getMessage(), e);

				throw new RuntimeException(e);
			} finally {
				monitor.done();
			}
		}
	}

	private final List<IJobControlListener> listeners = new LinkedList<>();
	private final Runner runner;
	private EJobState currentState = EJobState.UNKNOWN;
	private double progress = 0;

	protected AbstractEclipseJobControl(final String jobName) {
		this(jobName, Collections.<QualifiedName, Object>emptyMap());
	}

	protected abstract void doRunJob(IProgressMonitor progressMonitor);

	/**
	 * Constructor taking a map of properties to pass into the internal {@link Job}
	 * 
	 * @param jobName
	 * @param jobProperties
	 */
	protected AbstractEclipseJobControl(final String jobName, final Map<QualifiedName, Object> jobProperties) {
		super();
		runner = new Runner(jobName);
		for (final Map.Entry<QualifiedName, Object> entry : jobProperties.entrySet()) {
			runner.setProperty(entry.getKey(), entry.getValue());
		}
		currentState = EJobState.CREATED;
	}

	public void setRule(final ISchedulingRule rule) {
		runner.setRule(rule);
	}

	public ISchedulingRule getRule() {
		return runner.getRule();
	}

	private synchronized void setJobState(final EJobState newState) {
		final EJobState oldState = currentState;
		if (oldState != newState) {

			currentState = newState;

			// Take copy to avoid concurrent modification exceptions.
			final List<IJobControlListener> copy = new ArrayList<>(listeners);

			for (final IJobControlListener mjl : copy) {
				try {
					if (!mjl.jobStateChanged(this, oldState, newState)) {
						listeners.remove(mjl);
					}
				} catch (final Exception e) {
					LOG.error(String.format("Error in job state listener %s", e.getMessage()), e);
				}
			}
		}
	}

	@Override
	public void prepare() {
		setJobState(EJobState.INITIALISING);
		reallyPrepare();
		setJobState(EJobState.INITIALISED);
	}

	@Override
	public synchronized void start() {
		switch (getJobState()) {
		case UNKNOWN, INITIALISING:
			this.addListener(new IJobControlListener() {
				@Override
				public boolean jobStateChanged(final IJobControl jobControl, final EJobState oldState, final EJobState newState) {
					if (newState == EJobState.INITIALISED) {
						runner.schedule();
						// return false to avoid getting any more events
						return false;
					}
					return true;
				}

				@Override
				public boolean jobProgressUpdated(final IJobControl jobControl, final int progressDelta) {
					return false;
				}
			});
			break;
		case INITIALISED:
			runner.schedule();
			break;
		default:
		}
	}

	@Override
	public void cancel() {
		switch (getJobState()) {
		case CANCELLING, CANCELLED:
			return;
		case RUNNING:
			runner.cancel();
			setJobState(EJobState.CANCELLING);
			break;
		default:
			setJobState(EJobState.CANCELLING);
			setJobState(EJobState.CANCELLED);
		}
	}

	@Override
	public synchronized EJobState getJobState() {
		return currentState;
	}

	@Override
	public void addListener(final IJobControlListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(final IJobControlListener listener) {
		listeners.remove(listener);
	}

	@Override
	public int getProgress() {
		return (int) Math.round(progress);
	}

	protected void setProgress(final double newProgress) {
		final double delta = newProgress - progress;
		progress = newProgress;
		// Take copy to avoid concurrent modification exceptions.
		final List<IJobControlListener> copy = new ArrayList<>(listeners);

		for (final IJobControlListener mjl : copy) {
			if (!mjl.jobProgressUpdated(this, (int) Math.round(delta))) {
				listeners.remove(mjl);
			}
		}
	}

	@Override
	public void dispose() {

		runner.cancel();
		this.currentState = EJobState.UNKNOWN;
		this.listeners.clear();
	}

	/**
	 * This method should prepare the job for execution
	 */
	protected abstract void reallyPrepare();

	public void setJobProperty(final QualifiedName name, final Object value) {
		if (this.runner != null) {
			runner.setProperty(name, value);
		}
	}

	public Object getJobProperty(final QualifiedName name) {
		if (this.runner != null) {
			return runner.getProperty(name);
		}
		return null;
	}
}
