/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.jobcontroller.core;


/**
 * A managed job. The lifecycle of a managed job goes
 * <ol>
 * <li>prepare()</li>
 * <li>start()</li>
 * <li>some amount of pause() and resume()</li>
 * <li>possible cancel()</li>
 * </ol>
 * @author hinton
 *
 */
public interface IManagedJob {
	public enum JobState {
		UNKNOWN, INITIALISED, RUNNING, PAUSING, PAUSED, RESUMING, COMPLETED, CANCELLING, CANCELLED
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

	int getProgress();

	void addListener(IManagedJobListener listener);

	void removeListener(IManagedJobListener listener);

	void dispose();
}
