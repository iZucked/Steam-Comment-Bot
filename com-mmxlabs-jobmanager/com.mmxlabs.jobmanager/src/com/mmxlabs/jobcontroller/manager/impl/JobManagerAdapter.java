/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobcontroller.manager.impl;

import com.mmxlabs.jobcontroller.jobs.IJobControl;
import com.mmxlabs.jobcontroller.jobs.IJobDescriptor;
import com.mmxlabs.jobcontroller.manager.IJobManager;
import com.mmxlabs.jobcontroller.manager.IJobManagerListener;

/**
 * Implementation of {@link IJobManagerListener} with stub methods. Not intended to be used directly, rather sub-classes with the relevant methods overridden.
 * 
 * @author Simon Goodall
 */
public class JobManagerAdapter implements IJobManagerListener {

	@Override
	public void jobAdded(final IJobManager jobManager, final IJobDescriptor job, final IJobControl control) {

	}

	@Override
	public void jobRemoved(final IJobManager jobManager, final IJobDescriptor job, final IJobControl control) {

	}
}
