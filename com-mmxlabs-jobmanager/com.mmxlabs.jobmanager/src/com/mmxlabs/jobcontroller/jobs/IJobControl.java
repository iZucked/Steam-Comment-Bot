/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobcontroller.jobs;

/**
 * A managed job. The lifecycle of a managed job goes
 * <ol>
 * <li>prepare()</li>
 * <li>start()</li>
 * <li>some amount of pause() and resume()</li>
 * <li>possible cancel()</li>
 * </ol>
 * 
 * @author hinton
 * 
 */
public interface IJobControl {
	public enum JobState {
		UNKNOWN, CREATED, INITIALISED, RUNNING, PAUSING, PAUSED, RESUMING, COMPLETED, CANCELLING, CANCELLED, INITIALISING
	}

	/**
	 * @return A descriptive name for this job.
	 */
	public String getJobName();

	void prepare();

	void start();

	void cancel();

	void pause();

	void resume();

	JobState getJobState();

	/**
	 * Returns progress as percentage
	 * 
	 * @return
	 */
	int getProgress();

	void addListener(IJobControlListener listener);

	void removeListener(IJobControlListener listener);

	void dispose();
}
