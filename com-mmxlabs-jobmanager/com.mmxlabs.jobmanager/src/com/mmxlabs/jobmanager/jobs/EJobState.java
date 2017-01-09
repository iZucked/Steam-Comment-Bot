/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.jobs;

/**
 * An Enum describing the different states an {@link IJobControl} instance can be in.
 * 
 * @author Simon Goodall
 * 
 */
public enum EJobState {
	/**
	 * Job is in an unknown state, possibly because it is still being created.
	 */
	UNKNOWN,
	/**
	 * Job has been created with the minimum effort required to be valid. It will need to enter the {@link EJobState#INITALISED} before it can be run.
	 */
	CREATED,

	/**
	 * THe Job is in the process of initialising, but is not quite ready to be run yet. This may be a long running process due to e.g. loading data from disk etc.
	 */
	INITIALISING,
	/**
	 * Job has been initialised and it is ready to be run.
	 */
	INITIALISED,
	/**
	 * The job is currently running
	 */
	RUNNING,
	/**
	 * The job is currently attempting to pause and will transition to the PAUSED state
	 */
	PAUSING,
	/**
	 * The job is currently paused
	 */
	PAUSED,
	/**
	 * The job is currently resuming from a PAUSED state and will transition back to the RUNNING state
	 */
	RESUMING,
	/**
	 * The job has successfully completed.
	 */
	COMPLETED,
	/**
	 * The job has been cancelled or aborted as is currently terminating
	 */
	CANCELLING,
	/**
	 * The job has been cancelled or aborted for some reason.
	 */
	CANCELLED,
}