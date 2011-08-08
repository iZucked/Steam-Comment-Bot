/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobcontroller.jobs;

import com.mmxlabs.jobcontroller.jobs.IManagedJob.JobState;


public interface IManagedJobListener {
	/**
	 * Callback when the specified job state changes. Returns true if the
	 * listener is to be kept. Return false to de-register listener.
	 * 
	 * @param job
	 * @param oldState
	 * @param newState
	 * @return
	 */
	boolean jobStateChanged(IManagedJob job, JobState oldState,
			JobState newState);

	/**
	 * Callback when the specified job progress changes. Returns true if the
	 * listener is to be kept. Return false to de-register listener.
	 * 
	 * @param job
	 * @param oldState
	 * @param newState
	 * @return
	 */
	boolean jobProgressUpdated(IManagedJob job, int progressDelta);
}
