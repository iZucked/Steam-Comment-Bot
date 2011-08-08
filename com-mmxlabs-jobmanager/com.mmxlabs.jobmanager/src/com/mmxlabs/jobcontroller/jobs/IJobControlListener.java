/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobcontroller.jobs;

import com.mmxlabs.jobcontroller.jobs.IJobControl.JobState;


public interface IJobControlListener {
	/**
	 * Callback when the specified job state changes. Returns true if the
	 * listener is to be kept. Return false to de-register listener.
	 * 
	 * @param job
	 * @param oldState
	 * @param newState
	 * @return
	 */
	boolean jobStateChanged(IJobControl job, JobState oldState,
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
	boolean jobProgressUpdated(IJobControl job, int progressDelta);
}
