/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobcontroller.core;

import java.util.List;

import org.eclipse.core.resources.IResource;

public interface IJobManager {

	/**
	 * Returns the list of {@link IManagedJob} added to the {@link IJobManager} instance. Will return an ermpty {@link List} if there are no {@link IManagedJob}s.
	 * 
	 * @return
	 */
	List<IManagedJob> getJobs();

	void addJob(IManagedJob job, IResource resource);

	void removeJob(IManagedJob job);

	void addJobManagerListener(IJobManagerListener jobManagerListener);

	void removeJobManagerListener(IJobManagerListener jobManagerListener);

	void toggleJobSelection(IManagedJob job);

	void toggleResourceSelection(IResource resource);

	void setJobSelection(IManagedJob job, boolean selected);

	void setResourceSelection(IResource resource, boolean selected);

	List<IManagedJob> getSelectedJobs();

	List<IResource> getSelectedResources();

	IManagedJob findJobForResource(IResource resource);

	IResource findResourceForJob(IManagedJob job);

}
