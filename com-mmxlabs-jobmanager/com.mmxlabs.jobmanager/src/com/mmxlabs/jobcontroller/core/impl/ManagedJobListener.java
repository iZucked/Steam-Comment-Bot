/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobcontroller.core.impl;

import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.jobcontroller.core.IManagedJob.JobState;
import com.mmxlabs.jobcontroller.core.IManagedJobListener;

/**
 * Implementation of {@link IManagedJobListener} with stub methods. Not intended
 * to be used directly, rather sub-classes with the relevant methods overridden.
 * 
 * @author Simon Goodall
 */
public class ManagedJobListener implements IManagedJobListener {

	@Override
	public boolean jobStateChanged(final IManagedJob job, final JobState oldState,
			final JobState newState) {
		return true;
	}

	@Override
	public boolean jobProgressUpdated(final IManagedJob job,
			final int progressDelta) {
		return true;
	}
}
