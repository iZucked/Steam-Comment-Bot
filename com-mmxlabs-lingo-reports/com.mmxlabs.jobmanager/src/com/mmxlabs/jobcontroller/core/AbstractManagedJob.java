/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */

package com.mmxlabs.jobcontroller.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

/**
 * An abstract class which provides most of what you want for any job.
 * 
 * Subclasses should only need to implement the missing methods.
 * 
 * @author hinton
 * 
 */
public abstract class AbstractManagedJob implements IManagedJob {
	private Throwable causeOfDeath = null;

	private class Runner extends Job {
		public Runner(final String name) {
			super(name);
		}

		private boolean paused = false;
		@Override
		public IStatus run(final IProgressMonitor monitor) {
			try {
				while (true) {
					if (monitor.isCanceled()) {
						kill();
						setJobState(JobState.CANCELLED);
						return Status.CANCEL_STATUS;
					} else if (paused) {
						synchronized (this) {
							if (paused) {
								setJobState(JobState.PAUSED);
								wait();
							}
						}
					} else {
						setJobState(JobState.RUNNING);
						final int p0 = getProgress();
						final boolean more = step();
						final int p1 = getProgress();
						if (p1 > p0) monitor.worked(p1 - p0);
						if (more == false) {
							setJobState(JobState.COMPLETED);
							return Status.OK_STATUS;
						}
						
					}
				}
			} catch (final InterruptedException e) {
				kill();
				return Status.CANCEL_STATUS;
			} catch (final Throwable ex) {
				die(ex);
				return Status.CANCEL_STATUS;
			} finally {
				monitor.done();
			}
		}
		
		public synchronized void pause() {
			paused = true;
		}

		public synchronized void resume() {
			paused = false;
			notifyAll();
		}

		@Override
		protected void canceling() {
			super.canceling();
			resume();
		}
	}

	private final LinkedList<IManagedJobListener> listeners = new LinkedList<IManagedJobListener>();
	private final Runner runner;
	private final String jobName;
	private JobState currentState = JobState.UNKNOWN;
	private int progress = 0;

	
	public AbstractManagedJob(final String jobName) {
		super();
		this.jobName = jobName;
		runner = new Runner(jobName);
		currentState = JobState.CREATED;
	}
	

	@Override
	public String getJobName() {
		return jobName;
	}

	private synchronized void setJobState(final JobState newState) {
		final JobState oldState = currentState;
		if (oldState != newState) {
			
			currentState = newState;
			
			// Take copy to avoid concurrent modification exceptions. 
			final List<IManagedJobListener> copy = new ArrayList<IManagedJobListener>(listeners);
			
			for (final IManagedJobListener mjl : copy) {
				if (!mjl.jobStateChanged(this, oldState, newState)) {
					listeners.remove(mjl);
				}
			}
		}
	}

	public Throwable getCauseOfError() {
		return causeOfDeath;
	}

	private void die(final Throwable ex) {
		causeOfDeath = ex;
		kill();
		setJobState(JobState.ERROR);
	}

	@Override
	public void prepare() {
		try {
			setJobState(JobState.INITIALISING);
			reallyPrepare();
			setJobState(JobState.INITIALISED);
		} catch (final Throwable ex) {
			die(ex);
		}
	}

	@Override
	public synchronized void start() {
		if (currentState != JobState.INITIALISED) {
			// we are probably preparing, so add a listener which waits
			this.addListener(new IManagedJobListener() {
				@Override
				public boolean jobStateChanged(IManagedJob job, JobState oldState,
						JobState newState) {
					if (newState == JobState.INITIALISED) {
						runner.schedule();
						return false; // return false to avoid getting any more events
					}
					return true;
				}
				
				@Override
				public boolean jobProgressUpdated(IManagedJob job, int progressDelta) {
					return false;
				}
			});
		} else {
			runner.schedule();
		}
	}

	@Override
	public void cancel() {
		runner.cancel();
		setJobState(JobState.CANCELLING);
	}

	@Override
	public void pause() {
		setJobState(JobState.PAUSING);
		runner.pause();
	}

	@Override
	public void resume() {
		setJobState(JobState.RESUMING);
		runner.resume();
	}

	@Override
	public JobState getJobState() {
		return currentState;
	}

	@Override
	public void addListener(final IManagedJobListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(final IManagedJobListener listener) {
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
		final List<IManagedJobListener> copy = new ArrayList<IManagedJobListener>(listeners);
		
		for (final IManagedJobListener mjl : copy) {
			if (!mjl.jobProgressUpdated(this, delta)) {
				listeners.remove(mjl);
			}
		}
	}

	@Override
	public void dispose() {
		
		runner.cancel();
		// TODO: this.runner = null;
		this.currentState = JobState.UNKNOWN;
		this.listeners.clear();
	}
	
	/**
	 * This method should prepare the job for execution
	 */
	protected abstract void reallyPrepare();

	/**
	 * This method should do a reasonably-sized unit of work
	 * 
	 * @return whether there are more units of work to do; return value of false
	 *         means finished.
	 */
	protected abstract boolean step();

	/**
	 * This method will be called if the job gets killed; clean up any
	 * resources.
	 */
	protected abstract void kill();
}
