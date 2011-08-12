package com.mmxlabs.jobmanager.manager.impl;

import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.jobmanager.manager.IJobManager;
import com.mmxlabs.jobmanager.manager.IJobManagerListener;

/**
 * A {@link IJobManagerListener} implementation which automatically calls {@link IJobDescriptor#dispose()} when the job is removed from the {@link IJobManager}. The listener will then remove itself
 * from the {@link IJobManager}.
 * 
 * @author Simon Goodall
 * 
 */
public final class DisposeOnRemoveListener implements IJobManagerListener {

	private final IJobDescriptor job;

	public DisposeOnRemoveListener(final IJobDescriptor job) {
		this.job = job;
	}

	@Override
	public void jobAdded(final IJobManager jobManager, final IJobDescriptor job, final IJobControl control) {

	}

	@Override
	public void jobRemoved(final IJobManager jobManager, final IJobDescriptor job, final IJobControl control) {

		// If this is the job being removed, then dispose and remove
		// references to it
		// TODO: Should this be .equals?
		if (this.job == job) {
			jobManager.removeJobManagerListener(this);
			job.dispose();
		}
	}
}