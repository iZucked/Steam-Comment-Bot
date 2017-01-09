/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.eclipse.manager;

import java.util.Collection;
import java.util.List;

import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.jobmanager.manager.IJobManager;

public interface IEclipseJobManager {

	/**
	 * Returns the list of {@link IManagedJob} added to the {@link IEclipseJobManager} instance. Will return an empty {@link List} if there are no {@link IManagedJob}s.
	 * 
	 * @return
	 */
	List<IJobDescriptor> getJobs();

	IJobControl submitJob(IJobDescriptor job, Object resource);

	void removeJob(IJobDescriptor job);

	void addEclipseJobManagerListener(IEclipseJobManagerListener jobManagerListener);

	void removeEclipseJobManagerListener(IEclipseJobManagerListener jobManagerListener);

	IJobDescriptor findJobForResource(Object resource);

	Object findResourceForJob(IJobDescriptor job);

	IJobControl getControlForJob(IJobDescriptor jobDescriptor);

	Collection<IJobManager> getJobManagers();
}
