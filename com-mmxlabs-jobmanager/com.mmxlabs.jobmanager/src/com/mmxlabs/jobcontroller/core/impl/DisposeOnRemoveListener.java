package com.mmxlabs.jobcontroller.core.impl;

import org.eclipse.core.resources.IResource;

import com.mmxlabs.jobcontroller.core.IJobManager;
import com.mmxlabs.jobcontroller.core.IJobManagerListener;
import com.mmxlabs.jobcontroller.core.IManagedJob;

/**
 * A {@link IJobManagerListener} implementation which automatically calls {@link IManagedJob#dispose()} when the job is removed from the {@link IJobManager}. The listener will then remove itself from
 * the {@link IJobManager}.
 * 
 * @author Simon Goodall
 * 
 */
public final class DisposeOnRemoveListener implements IJobManagerListener {
	
	private final IManagedJob newJob;

	public DisposeOnRemoveListener(final IManagedJob newJob) {
		this.newJob = newJob;
	}

	@Override
	public void jobSelected(final IJobManager jobManager, final IManagedJob job, final IResource resource) {

	}

	@Override
	public void jobRemoved(final IJobManager jobManager, final IManagedJob job, final IResource resource) {

		// If this is the job being removed, then dispose and remove
		// references to it
		if (job == newJob) {
			jobManager.removeJobManagerListener(this);
			newJob.dispose();
		}
	}

	@Override
	public void jobDeselected(final IJobManager jobManager, final IManagedJob job, final IResource resource) {

	}

	@Override
	public void jobAdded(final IJobManager jobManager, final IManagedJob job, final IResource resource) {

	}
}