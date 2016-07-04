/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.manager;

import java.util.Collection;
import java.util.List;

import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;

/**
 * An {@link IJobManager} manages the execution of {@link IJobDescriptor}s. On Submission of a {@link IJobDescriptor} a {@link IJobControl} will be returned to control execution of the job.
 * 
 * @author Simon Goodall
 * 
 */
public interface IJobManager {

	/**
	 * Return the {@link IJobManagerDescriptor} describing this job manager instance.
	 * 
	 * @return
	 */
	IJobManagerDescriptor getJobManagerDescriptor();

	/**
	 * Returns the list of {@link IManagedJob} added to the {@link IJobManager} instance. Will return an empty {@link List} if there are no {@link IManagedJob}s.
	 * 
	 * @return
	 */
	Collection<IJobDescriptor> getJobs();

	/**
	 * Submit a new {@link IJobDescriptor} to the manager. An {@link IJobControl} reference is returned.
	 * 
	 * @param job
	 * @return
	 */
	IJobControl submitJob(IJobDescriptor job);

	/**
	 * Find and return any {@link IJobControl}s for the {@link IJobDescriptor}. Returns null if none are found - indicating that the job has not been successfully submitted.
	 * 
	 * @param job
	 * @return
	 */
	IJobControl getJobControl(IJobDescriptor job);

	/**
	 * Removes the {@link IJobControl} (and cancels associated job) linked to this {@link IJobDescriptor}. {@link IJobControl} instance will be unusable after this call.
	 * 
	 * @param job
	 */
	void removeJobDescriptor(IJobDescriptor job);

	/**
	 * Add a listener for {@link IJobManager} instances to track addition and removal of jobs.
	 * 
	 * @param listener
	 */
	void addJobManagerListener(IJobManagerListener jobManagerListener);

	/**
	 * Remove a previous registered listener
	 * 
	 * @param listener
	 */
	void removeJobManagerListener(IJobManagerListener jobManagerListener);
}
