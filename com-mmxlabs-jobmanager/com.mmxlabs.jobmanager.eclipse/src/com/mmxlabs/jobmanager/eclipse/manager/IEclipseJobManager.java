/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
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
	 * Returns the list of {@link IManagedJob} added to the {@link IEclipseJobManager} instance. Will return an ermpty {@link List} if there are no {@link IManagedJob}s.
	 * 
	 * @return
	 */
	List<IJobDescriptor> getJobs();

	IJobControl submitJob(IJobDescriptor job, Object resource);

	void removeJob(IJobDescriptor job);
	

	void addEclipseJobManagerListener(IEclipseJobManagerListener jobManagerListener);

	void removeEclipseJobManagerListener(IEclipseJobManagerListener jobManagerListener);
	

	void toggleJobSelection(IJobDescriptor job);

	void toggleResourceSelection(Object resource);

	void setJobSelection(IJobDescriptor job, boolean selected);

	void setResourceSelection(Object resource, boolean selected);

	List<IJobDescriptor> getSelectedJobs();

	List<Object> getSelectedResources();

	
	IJobDescriptor findJobForResource(Object resource);

	Object findResourceForJob(IJobDescriptor job);

	IJobControl getControlForJob(IJobDescriptor jobDescriptor);

	Collection<IJobManager> getJobManagers();
}
