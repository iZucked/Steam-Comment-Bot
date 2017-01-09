/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.manager.impl;

import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.jobmanager.manager.IJobManager;
import com.mmxlabs.jobmanager.manager.IJobManagerListener;

/**
 * Implementation of {@link IJobManagerListener} with stub methods. Not intended to be used directly, rather sub-classes with the relevant methods overridden.
 * 
 * @author Simon Goodall
 */
public class JobManagerAdapter implements IJobManagerListener {

	@Override
	public void jobAdded(final IJobManager jobManager, final IJobDescriptor job, final IJobControl control) {
		// Clients should override
	}

	@Override
	public void jobRemoved(final IJobManager jobManager, final IJobDescriptor job, final IJobControl control) {
		// Clients should override
	}
}
