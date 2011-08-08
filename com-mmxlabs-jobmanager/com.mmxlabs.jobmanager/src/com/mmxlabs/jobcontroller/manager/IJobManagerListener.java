/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobcontroller.manager;

import org.eclipse.core.resources.IResource;

import com.mmxlabs.jobcontroller.jobs.IJobControl;


public interface IJobManagerListener {

	void jobAdded(IJobManager jobManager, IJobControl job, IResource resource);

	void jobRemoved(IJobManager jobManager, IJobControl job, IResource resource);
	
	void jobSelected(IJobManager jobManager, IJobControl job, IResource resource);
	
	void jobDeselected(IJobManager jobManager, IJobControl job, IResource resource);
}