package com.mmxlabs.jobcontroller.jobs.impl;

import org.eclipse.core.resources.IResource;

import com.mmxlabs.jobcontroller.jobs.IJobControl;
import com.mmxlabs.jobcontroller.manager.IJobManager;
import com.mmxlabs.jobcontroller.manager.IJobManagerListener;

/**
 * A {@link IJobManagerListener} implementation which automatically calls {@link IJobControl#dispose()} when the job is removed from the {@link IJobManager}. The listener will then remove itself from
 * the {@link IJobManager}.
 * 
 * @author Simon Goodall
 * 
 */
public final class DisposeOnRemoveListener implements IJobManagerListener {
	
	private final IJobControl newJob;

	public DisposeOnRemoveListener(final IJobControl newJob) {
		this.newJob = newJob;
	}

	@Override
	public void jobSelected(final IJobManager jobManager, final IJobControl job, final IResource resource) {

	}

	@Override
	public void jobRemoved(final IJobManager jobManager, final IJobControl job, final IResource resource) {

		// If this is the job being removed, then dispose and remove
		// references to it
		if (job == newJob) {
			jobManager.removeJobManagerListener(this);
			newJob.dispose();
		}
	}

	@Override
	public void jobDeselected(final IJobManager jobManager, final IJobControl job, final IResource resource) {

	}

	@Override
	public void jobAdded(final IJobManager jobManager, final IJobControl job, final IResource resource) {

	}
}