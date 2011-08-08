/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobcontroller.jobs.impl;

import com.mmxlabs.jobcontroller.jobs.EJobState;
import com.mmxlabs.jobcontroller.jobs.IJobControl;
import com.mmxlabs.jobcontroller.jobs.IJobControlListener;

/**
 * Implementation of {@link IJobControlListener} with stub methods. Not intended to be used directly, rather sub-classes with the relevant methods overridden.
 * 
 * @author Simon Goodall
 */
public class JobControlAdapter implements IJobControlListener {

	@Override
	public boolean jobStateChanged(final IJobControl control, final EJobState oldState, final EJobState newState) {
		return true;
	}

	@Override
	public boolean jobProgressUpdated(final IJobControl control, final int progressDelta) {
		return true;
	}
}
