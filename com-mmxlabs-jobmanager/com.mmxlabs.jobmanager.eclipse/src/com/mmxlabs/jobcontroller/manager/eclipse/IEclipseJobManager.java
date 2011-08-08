/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobcontroller.manager.eclipse;

import java.util.List;

import org.eclipse.core.resources.IResource;

import com.mmxlabs.jobcontroller.jobs.IJobControl;
import com.mmxlabs.jobcontroller.jobs.IJobDescriptor;

public interface IEclipseJobManager {

	/**
	 * Returns the list of {@link IManagedJob} added to the {@link IEclipseJobManager} instance. Will return an ermpty {@link List} if there are no {@link IManagedJob}s.
	 * 
	 * @return
	 */
	List<IJobDescriptor> getJobs();

	void submitJob(IJobDescriptor job, IResource resource);

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

}
