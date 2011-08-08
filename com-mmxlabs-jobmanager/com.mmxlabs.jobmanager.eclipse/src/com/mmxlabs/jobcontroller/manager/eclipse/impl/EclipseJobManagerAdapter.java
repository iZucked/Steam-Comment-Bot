/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobcontroller.manager.eclipse.impl;

import org.eclipse.core.resources.IResource;

import com.mmxlabs.jobcontroller.jobs.IJobControl;
import com.mmxlabs.jobcontroller.jobs.IJobDescriptor;
import com.mmxlabs.jobcontroller.manager.eclipse.IEclipseJobManager;
import com.mmxlabs.jobcontroller.manager.eclipse.IEclipseJobManagerListener;

/**
 * Implementation of {@link IEclipseJobManagerListener} with stub methods. Not intended to be used directly, rather sub-classes with the relevant methods overridden.
 * 
 * @author Simon Goodall
 */
public class EclipseJobManagerAdapter implements IEclipseJobManagerListener {

	@Override
	public void jobAdded(final IEclipseJobManager jobManager, final IJobDescriptor job, final IJobControl control, final IResource resource) {

	}

	@Override
	public void jobRemoved(final IEclipseJobManager jobManager, final IJobDescriptor job, final IJobControl control, final IResource resource) {

	}

	@Override
	public void jobSelected(final IEclipseJobManager jobManager, final IJobDescriptor job, final IJobControl control, final IResource resource) {

	}

	@Override
	public void jobDeselected(final IEclipseJobManager jobManager, final IJobDescriptor job, final IJobControl control, final IResource resource) {

	}
}
