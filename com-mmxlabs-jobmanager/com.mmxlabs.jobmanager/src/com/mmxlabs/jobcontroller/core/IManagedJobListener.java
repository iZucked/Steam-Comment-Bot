/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobcontroller.core;

import com.mmxlabs.jobcontroller.core.IManagedJob.JobState;

public interface IManagedJobListener {
	boolean jobStateChanged(IManagedJob job, JobState oldState, JobState newState);
	boolean jobProgressUpdated(IManagedJob job, int progressDelta);
}
