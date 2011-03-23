package com.mmxlabs.jobcontroller.core.impl;

import org.eclipse.core.resources.IResource;

import com.mmxlabs.jobcontroller.core.IJobManager;
import com.mmxlabs.jobcontroller.core.IJobManagerListener;
import com.mmxlabs.jobcontroller.core.IManagedJob;

/**
 * Implementation of {@link IJobManagerListener} with stub methods. Not intended
 * to be used directly, rather sub-classes with the relevant methods overridden.
 * 
 * @author Simon Goodall
 */
public class JobManagerListener implements IJobManagerListener {

	@Override
	public void jobAdded(final IJobManager jobManager, final IManagedJob job,
			final IResource resource) {

	}

	@Override
	public void jobRemoved(final IJobManager jobManager, final IManagedJob job,
			final IResource resource) {

	}

	@Override
	public void jobSelected(final IJobManager jobManager,
			final IManagedJob job, final IResource resource) {

	}

	@Override
	public void jobDeselected(final IJobManager jobManager,
			final IManagedJob job, final IResource resource) {

	}
}
