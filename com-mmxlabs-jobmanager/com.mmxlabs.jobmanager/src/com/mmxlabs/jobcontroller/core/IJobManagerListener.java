/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.jobcontroller.core;

public interface IJobManagerListener {

	void jobAdded(IJobManager jobManager, IManagedJob job);

	void jobRemoved(IJobManager jobManager, IManagedJob job);
	
	void jobSelected(IJobManager jobManager, IManagedJob job);
	
	void jobDeselected(IJobManager jobManager, IManagedJob job);
}