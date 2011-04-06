/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobcontroller.core;

import org.eclipse.core.resources.IResource;


public interface IJobManagerListener {

	void jobAdded(IJobManager jobManager, IManagedJob job, IResource resource);

	void jobRemoved(IJobManager jobManager, IManagedJob job, IResource resource);
	
	void jobSelected(IJobManager jobManager, IManagedJob job, IResource resource);
	
	void jobDeselected(IJobManager jobManager, IManagedJob job, IResource resource);
}