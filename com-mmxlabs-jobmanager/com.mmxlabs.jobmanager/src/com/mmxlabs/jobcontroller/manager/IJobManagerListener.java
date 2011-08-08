/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobcontroller.manager;

import org.eclipse.core.resources.IResource;

import com.mmxlabs.jobcontroller.jobs.IManagedJob;


public interface IJobManagerListener {

	void jobAdded(IJobManager jobManager, IManagedJob job, IResource resource);

	void jobRemoved(IJobManager jobManager, IManagedJob job, IResource resource);
	
	void jobSelected(IJobManager jobManager, IManagedJob job, IResource resource);
	
	void jobDeselected(IJobManager jobManager, IManagedJob job, IResource resource);
}