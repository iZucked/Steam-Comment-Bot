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
	public void jobStateChanged(final IManagedJob job, final JobState oldState,
			final JobState newState) {

	}

	@Override
	public void jobProgressUpdated(final IManagedJob job,
			final int progressDelta) {

	}
}
