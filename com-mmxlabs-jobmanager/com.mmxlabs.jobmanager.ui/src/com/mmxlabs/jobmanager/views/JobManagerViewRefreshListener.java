/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.views;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManagerListener;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.jobmanager.manager.IJobManager;

class JobManagerViewRefreshListener implements IEclipseJobManagerListener {

	private final JobManagerView view;
	private final IJobControlListener jobListener;

	public JobManagerViewRefreshListener(final JobManagerView view, final IJobControlListener jobListener) {
		this.view = view;
		this.jobListener = jobListener;
	}

	@Override
	public void jobRemoved(final IEclipseJobManager jobManager, final IJobDescriptor job, final IJobControl control, final Object resource) {

		control.removeListener(jobListener);
		view.refresh();
	}

	@Override
	public void jobAdded(final IEclipseJobManager jobManager, final IJobDescriptor job, final IJobControl control, final Object resource) {

		control.addListener(jobListener);
		view.refresh();
	}

	@Override
	public void jobManagerAdded(final IEclipseJobManager eclipseJobManager, final IJobManager jobManager) {
		view.refresh();

	}

	@Override
	public void jobManagerRemoved(final IEclipseJobManager eclipseJobManager, final IJobManager jobManager) {
		view.refresh();

	}
}
