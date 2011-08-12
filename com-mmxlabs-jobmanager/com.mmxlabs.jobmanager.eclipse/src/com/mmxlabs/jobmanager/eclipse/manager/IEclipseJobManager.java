/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.eclipse.manager;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IResource;

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

	IJobControl submitJob(IJobDescriptor job, IResource resource);

	void removeJob(IJobDescriptor job);
	

	void addEclipseJobManagerListener(IEclipseJobManagerListener jobManagerListener);

	void removeEclipseJobManagerListener(IEclipseJobManagerListener jobManagerListener);
	

	void toggleJobSelection(IJobDescriptor job);

	void toggleResourceSelection(IResource resource);

	void setJobSelection(IJobDescriptor job, boolean selected);

	void setResourceSelection(IResource resource, boolean selected);

	List<IJobDescriptor> getSelectedJobs();

	List<IResource> getSelectedResources();

	
	IJobDescriptor findJobForResource(IResource resource);

	IResource findResourceForJob(IJobDescriptor job);

	IJobControl getControlForJob(IJobDescriptor jobDescriptor);

	Collection<IJobManager> getJobManagers();
}
