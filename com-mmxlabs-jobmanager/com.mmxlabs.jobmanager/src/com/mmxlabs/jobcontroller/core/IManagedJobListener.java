/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.jobcontroller.core;

public interface IManagedJobListener {

	void jobStarted(IManagedJob job);
	
	void jobStopped(IManagedJob job);
	
	void jobPaused(IManagedJob job);
	
	void jobResumed(IManagedJob job);
	
	void jobProgressUpdate(IManagedJob job, int progressDelta);
	
	void jobCompleted(IManagedJob job);

	void jobCancelled(IManagedJob job);

	void jobPausing(IManagedJob job);

	void jobResuming(IManagedJob job);
}
