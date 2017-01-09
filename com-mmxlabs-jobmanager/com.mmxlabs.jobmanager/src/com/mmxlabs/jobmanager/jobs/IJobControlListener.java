/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.jobs;

/**
 * A listener for {@link IJobControl} instances to track changes in state and progress.
 * 
 * @author Simon Goodall
 * 
 */
public interface IJobControlListener {
	/**
	 * Callback when the specified job state changes. Returns true if the listener is to be kept. Return false to de-register listener.
	 * 
	 * @param job
	 * @param oldState
	 * @param newState
	 * @return
	 */
	boolean jobStateChanged(IJobControl job, EJobState oldState, EJobState newState);

	/**
	 * Callback when the specified job progress changes. Returns true if the listener is to be kept. Return false to de-register listener.
	 * 
	 * @param job
	 * @param oldState
	 * @param newState
	 * @return
	 */
	boolean jobProgressUpdated(IJobControl job, int progressDelta);
}
