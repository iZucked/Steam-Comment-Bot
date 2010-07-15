package com.mmxlabs.jobcontroller.core;

/**
 * {@link IManagedJobListener} implementation to trigger a single abstract
 * method for any listener event.
 * 
 * @author sg
 * 
 */
public abstract class ManagedJobListenerNotifier implements IManagedJobListener {

	@Override
	public void jobStarted(IManagedJob job) {
		jobNotified(job);
	}

	@Override
	public void jobStopped(IManagedJob job) {
		jobNotified(job);
	}

	@Override
	public void jobPaused(IManagedJob job) {
		jobNotified(job);
	}

	@Override
	public void jobResumed(IManagedJob job) {
		jobNotified(job);
	}

	@Override
	public void jobProgressUpdate(IManagedJob job, int progressDelta) {
		jobNotified(job);
	}

	@Override
	public void jobCompleted(IManagedJob job) {
		jobNotified(job);
	}

	@Override
	public void jobCancelled(IManagedJob job) {
		jobNotified(job);
	}

	@Override
	public void jobPausing(IManagedJob job) {
		jobNotified(job);
	}

	@Override
	public void jobResuming(IManagedJob job) {
		jobNotified(job);
	}

	public abstract void jobNotified(IManagedJob job);

}
