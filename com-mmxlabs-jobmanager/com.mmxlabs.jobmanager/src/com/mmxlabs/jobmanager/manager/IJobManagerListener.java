/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.manager;

import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;

/**
 * A listener for {@link IJobManager} instances to track addition and removal of jobs.
 * 
 * @author Simon Goodall
 * 
 */
public interface IJobManagerListener {

	/**
	 * Callback fired when a new {@link IJobDescriptor} has been successfully submitted to the {@link IJobManager}
	 * 
	 * @param jobManager
	 * @param job
	 * @param control
	 */
	void jobAdded(IJobManager jobManager, IJobDescriptor job, IJobControl control);

	/**
	 * Callback fired when a {@link IJobDescriptor} has removed from the {@link IJobManager}
	 * 
	 * @param jobManager
	 * @param job
	 * @param control
	 */
	void jobRemoved(IJobManager jobManager, IJobDescriptor job, IJobControl control);
}