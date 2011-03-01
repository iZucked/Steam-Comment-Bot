/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.jobcontroller.core;

import com.mmxlabs.jobcontroller.core.IManagedJob.JobState;

public interface IManagedJobListener {
	void jobStateChanged(IManagedJob job, JobState oldState, JobState newState);
	void jobProgressUpdated(IManagedJob job, int progressDelta);
}
