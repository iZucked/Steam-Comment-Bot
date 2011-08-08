/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobcontroller.jobs.impl;

import com.mmxlabs.jobcontroller.jobs.IJobControl;
import com.mmxlabs.jobcontroller.jobs.IJobControl.JobState;
import com.mmxlabs.jobcontroller.jobs.IJobControlListener;

/**
 * Implementation of {@link IJobControlListener} with stub methods. Not intended
 * to be used directly, rather sub-classes with the relevant methods overridden.
 * 
 * @author Simon Goodall
 */
public class ManagedJobListener implements IJobControlListener {

	@Override
	public boolean jobStateChanged(final IJobControl job, final JobState oldState,
			final JobState newState) {
		return true;
	}

	@Override
	public boolean jobProgressUpdated(final IJobControl job,
			final int progressDelta) {
		return true;
	}
}
