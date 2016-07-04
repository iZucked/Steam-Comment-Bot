/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;

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

		private boolean paused = false;

		public Runner(final String name) {
			super(name);
		}

		@Override
		public IStatus run(final IProgressMonitor monitor) {
			monitor.beginTask("", 100);
			try {
				doRunJob(new SubProgressMonitor(monitor, 100) {
					@Override
					public void worked(int work) {
						super.worked(work);
						AbstractEclipseJobControl.this.setProgress(progress + work);
					}
				});
				if (monitor.isCanceled()) {
					kill();
					setJobState(EJobState.CANCELLED);
					return Status.CANCEL_STATUS;
				} else if (paused) {
					setJobState(EJobState.COMPLETED);
					setProgress(100);
					return Status.OK_STATUS;
				}
				setProgress(100);
				setJobState(EJobState.COMPLETED);
				return Status.OK_STATUS;
			} catch (final OperationCanceledException e) {
				kill();
				setJobState(EJobState.CANCELLED);
				return Status.CANCEL_STATUS;
			} catch (final Exception | AssertionError e) {
				LOG.error(e.getMessage(), e);
				kill();
				setJobState(EJobState.CANCELLED);

				throw new RuntimeException(e);
			} finally {
				monitor.done();
			}
//			return Status.OK_STATUS;
		}

		@Override
		protected void canceling() {
			super.canceling();
			resume();
		}
	}

	private final List<IJobControlListener> listeners = new LinkedList<IJobControlListener>();
	private final Runner runner;
	private EJobState currentState = EJobState.UNKNOWN;
	private int progress = 0;

	public AbstractEclipseJobControl(final String jobName) {
		this(jobName, Collections.<QualifiedName, Object> emptyMap());
	}

	protected abstract void doRunJob(IProgressMonitor progressMonitor);

	/**
	 * Constructor taking a map of properties to pass into the internal {@link Job}
	 * 
	 * @param jobName
	 * @param jobProperties
	 */
	public AbstractEclipseJobControl(final String jobName, final Map<QualifiedName, Object> jobProperties) {
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
			final List<IJobControlListener> copy = new ArrayList<IJobControlListener>(listeners);

			for (final IJobControlListener mjl : copy) {
				if (!mjl.jobStateChanged(this, oldState, newState)) {
					listeners.remove(mjl);
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
		case UNKNOWN:
		case INITIALISING:
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
		case CANCELLING:
		case CANCELLED:
			return;
		case PAUSED:
			resume();
			break;
		case RUNNING:
			runner.cancel();
			setJobState(EJobState.CANCELLING);
			break;
		default:
			setJobState(EJobState.CANCELLING);
			kill();
			setJobState(EJobState.CANCELLED);
		}
	}

	@Override
	public void pause() {
		// DO NOTHING
	}

	@Override
	public void resume() {
		// DO NOTHING
	}

	@Override
	public EJobState getJobState() {
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
		return progress;
	}

	protected void setProgress(final int newProgress) {
		final int delta = newProgress - progress;
		progress = newProgress;
		// Take copy to avoid concurrent modification exceptions.
		final List<IJobControlListener> copy = new ArrayList<IJobControlListener>(listeners);

		for (final IJobControlListener mjl : copy) {
			if (!mjl.jobProgressUpdated(this, delta)) {
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

	/**
	 * This method will be called if the job gets killed; clean up any resources.
	 */
	protected abstract void kill();

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
