/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.eclipse.manager;

import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.jobmanager.manager.IJobManager;

/**
 * Listener interface for {@link IEclipseJobManager}s
 * 
 * @author Simon Goodall
 * 
 */
public interface IEclipseJobManagerListener {

	/**
	 * Notify that a {@link IJobDescriptor} and {@link IJobControl} have been added to the {@link IEclipseJobManager}.
	 * 
	 * @param eclipseJobManager
	 * @param job
	 * @param control
	 * @param resource
	 */
	void jobAdded(IEclipseJobManager eclipseJobManager, IJobDescriptor job, IJobControl control, Object resource);

	/**
	 * Notify that a {@link IJobDescriptor} and {@link IJobControl} has been removed from the {@link IEclipseJobManager}.
	 * 
	 * @param eclipseJobManager
	 * @param job
	 * @param control
	 * @param resource
	 */
	void jobRemoved(IEclipseJobManager eclipseJobManager, IJobDescriptor job, IJobControl control, Object resource);

	/**
	 * Notify that a new {@link IJobManager} instance has been registered with the {@link IEclipseJobManager}
	 * 
	 * @param eclipseJobManager
	 * @param jobManager
	 */
	void jobManagerAdded(IEclipseJobManager eclipseJobManager, IJobManager jobManager);

	/**
	 * Notify that a {@link IJobManager} instance has been removed from the {@link IEclipseJobManager}
	 * 
	 * @param eclipseJobManager
	 * @param jobManager
	 */
	void jobManagerRemoved(IEclipseJobManager eclipseJobManager, IJobManager jobManager);
}