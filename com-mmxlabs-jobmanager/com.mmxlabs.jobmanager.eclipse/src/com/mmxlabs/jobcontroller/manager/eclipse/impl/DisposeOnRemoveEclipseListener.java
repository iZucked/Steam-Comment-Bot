package com.mmxlabs.jobcontroller.manager.eclipse.impl;

import org.eclipse.core.resources.IResource;

import com.mmxlabs.jobcontroller.jobs.IJobControl;
import com.mmxlabs.jobcontroller.jobs.IJobDescriptor;
import com.mmxlabs.jobcontroller.manager.IJobManager;
import com.mmxlabs.jobcontroller.manager.IJobManagerListener;
import com.mmxlabs.jobcontroller.manager.eclipse.IEclipseJobManager;

/**
 * A {@link IJobManagerListener} implementation which automatically calls {@link IJobDescriptor#dispose()} when the job is removed from the {@link IJobManager}. The listener will then remove itself
 * from the {@link IJobManager}.
 * 
 * @author Simon Goodall
 * 
 */
public final class DisposeOnRemoveEclipseListener extends EclipseJobManagerAdapter {

	private final IJobDescriptor job;

	public DisposeOnRemoveEclipseListener(final IJobDescriptor job) {
		this.job = job;
	}

	@Override
	public void jobRemoved(final IEclipseJobManager jobManager, final IJobDescriptor job, final IJobControl control, IResource resource) {

		// If this is the job being removed, then dispose and remove
		// references to it
		// TODO: Should this be .equals?
		if (this.job == job) {
			jobManager.removeEclipseJobManagerListener(this);
			job.dispose();
		}
	}
}