/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.eclipse.manager.impl;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManagerListener;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.jobmanager.manager.IJobManager;

/**
 * Implementation of {@link IEclipseJobManagerListener} with stub methods. Not intended to be used directly, rather sub-classes with the relevant methods overridden.
 * 
 * @author Simon Goodall
 * 
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public class EclipseJobManagerAdapter implements IEclipseJobManagerListener {

	@Override
	public void jobAdded(final IEclipseJobManager eclipseJobManager, final IJobDescriptor job, final IJobControl control, final Object resource) {
		// Clients should override
	}

	@Override
	public void jobRemoved(final IEclipseJobManager eclipseJobManager, final IJobDescriptor job, final IJobControl control, final Object resource) {
		// Clients should override
	}

	@Override
	public void jobManagerAdded(final IEclipseJobManager eclipseJobManager, final IJobManager jobManager) {
		// Clients should override
	}

	@Override
	public void jobManagerRemoved(final IEclipseJobManager eclipseJobManager, final IJobManager jobManager) {
		// Clients should override
	}
}