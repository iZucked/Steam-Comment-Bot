/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobcontroller.manager.impl;

import org.eclipse.core.resources.IResource;

import com.mmxlabs.jobcontroller.jobs.IJobControl;
import com.mmxlabs.jobcontroller.manager.IJobManager;
import com.mmxlabs.jobcontroller.manager.IJobManagerListener;

/**
 * Implementation of {@link IJobManagerListener} with stub methods. Not intended
 * to be used directly, rather sub-classes with the relevant methods overridden.
 * 
 * @author Simon Goodall
 */
public class JobManagerAdapter implements IJobManagerListener {

	@Override
	public void jobAdded(final IJobManager jobManager, final IJobControl job,
			final IResource resource) {

	}

	@Override
	public void jobRemoved(final IJobManager jobManager, final IJobControl job,
			final IResource resource) {

	}

	@Override
	public void jobSelected(final IJobManager jobManager,
			final IJobControl job, final IResource resource) {

	}

	@Override
	public void jobDeselected(final IJobManager jobManager,
			final IJobControl job, final IResource resource) {

	}
}
