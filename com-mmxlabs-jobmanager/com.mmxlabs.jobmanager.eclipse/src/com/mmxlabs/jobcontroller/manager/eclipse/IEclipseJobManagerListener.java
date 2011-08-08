/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobcontroller.manager.eclipse;

import org.eclipse.core.resources.IResource;

import com.mmxlabs.jobcontroller.jobs.IJobControl;
import com.mmxlabs.jobcontroller.jobs.IJobDescriptor;

public interface IEclipseJobManagerListener {

	void jobAdded(IEclipseJobManager jobManager, IJobDescriptor job, IJobControl control, IResource resource);

	void jobRemoved(IEclipseJobManager jobManager, IJobDescriptor job, IJobControl control, IResource resource);

	void jobSelected(IEclipseJobManager jobManager, IJobDescriptor job, IJobControl jobControl, IResource resource);

	void jobDeselected(IEclipseJobManager jobManager, IJobDescriptor job, IJobControl jobControl, IResource resource);
}