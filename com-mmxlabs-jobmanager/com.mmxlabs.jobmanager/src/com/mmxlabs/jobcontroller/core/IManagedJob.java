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
		UNKNOWN, INITIALISED, RUNNING, PAUSING, PAUSED, RESUMING, COMPLETED, CANCELLED
	}
	
	/**
	 * @return A descriptive name for this job.
	 */
	public String getJobName();
	
	public void prepare();
	public void start();
	public void cancel();
	public void pause();
	public void resume();
	
	public JobState getJobState();
	
	public int getProgress();
	
	public void addListener(IManagedJobListener listener);
	public void removeListener(IManagedJobListener listener);
}
