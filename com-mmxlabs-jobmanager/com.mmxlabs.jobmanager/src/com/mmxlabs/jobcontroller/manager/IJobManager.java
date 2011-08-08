/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobcontroller.manager;

import java.util.List;

import org.eclipse.core.resources.IResource;

import com.mmxlabs.jobcontroller.jobs.IJobControl;

public interface IJobManager {

	/**
	 * Returns the list of {@link IJobControl} added to the {@link IJobManager} instance. Will return an ermpty {@link List} if there are no {@link IJobControl}s.
	 * 
	 * @return
	 */
	List<IJobControl> getJobs();

	void addJob(IJobControl job, IResource resource);

	void removeJob(IJobControl job);

	void addJobManagerListener(IJobManagerListener jobManagerListener);

	void removeJobManagerListener(IJobManagerListener jobManagerListener);

	void toggleJobSelection(IJobControl job);

	void toggleResourceSelection(IResource resource);

	void setJobSelection(IJobControl job, boolean selected);

	void setResourceSelection(IResource resource, boolean selected);

	List<IJobControl> getSelectedJobs();

	List<IResource> getSelectedResources();

	IJobControl findJobForResource(IResource resource);

	IResource findResourceForJob(IJobControl job);

}
