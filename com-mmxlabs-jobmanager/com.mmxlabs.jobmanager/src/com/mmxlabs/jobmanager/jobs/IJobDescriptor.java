/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.jobs;

import com.mmxlabs.jobmanager.manager.IJobManager;

/**
 * Interface describing the definition of a Job. This is the client's reference of a job which will be submitted to a {@link IJobManager} for execution.
 * 
 * @author Simon Goodall
 */
public interface IJobDescriptor {
	/**
	 * @return A descriptive name for this job.
	 */
	public String getJobName();

	/**
	 * place holder method to return information such as creator, creation date etc..
	 * 
	 * @return
	 */
	Object getJobMetadata();

	/**
	 * Placeholder method to get information on the job. I.e. how to convert from a description into an actual job.
	 * 
	 * @return
	 */
	Object getJobType();

	/**
	 * Placeholder method for additional metadata on the job. Such data could include stuff similar to condors job config file. ... hints for running/eexcuting the job
	 * 
	 * @return
	 */
	Object getJobContext();

	/**
	 * Clean up
	 */
	void dispose();
}
