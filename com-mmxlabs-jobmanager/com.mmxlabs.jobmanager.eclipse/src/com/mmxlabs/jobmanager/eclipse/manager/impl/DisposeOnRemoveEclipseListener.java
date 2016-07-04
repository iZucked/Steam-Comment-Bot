/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.eclipse.manager.impl;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
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
public final class DisposeOnRemoveEclipseListener extends EclipseJobManagerAdapter {

	private final IJobDescriptor job;

	public DisposeOnRemoveEclipseListener(final IJobDescriptor job) {
		this.job = job;
	}

	@Override
	public void jobRemoved(final IEclipseJobManager jobManager, final IJobDescriptor job, final IJobControl control, final Object resource) {

		// If this is the job being removed, then dispose and remove
		// references to it
		// TODO: Should this be .equals?
		if (this.job == job) {
			jobManager.removeEclipseJobManagerListener(this);
			job.dispose();
		}
	}
}