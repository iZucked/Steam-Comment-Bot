/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.jobs;

import org.eclipse.jdt.annotation.NonNull;


/**
 * Interface to control the lifecycle of a job. The expected lifecycle is defined as;
 * <ol>
 * <li>prepare()</li>
 * <li>start()</li>
 * <li>some amount of pause() and resume()</li>
 * <li>possible cancel()</li>
 * </ol>
 * 
 * @author hinton
 * 
 */
public interface IJobControl {

/**
	 * Returns the {@link IJobDescriptor} linked to this {@link IJobControl
	 * @return
	 */
	@NonNull IJobDescriptor getJobDescriptor();

	/**
	 * Perform the required initialisation to be able to begin an optimisation. For example this may require loading data and preparing data structures so that an initial solution state can be
	 * evaluated before an optimisation occurs.
	 * 
	 */
	void prepare();

	/**
	 * Start an optimisation.
	 */
	void start();

	/**
	 * Abort and terminate the optimisation.
	 */
	void cancel();

	/**
	 * Returns true if this job can be paused and resumed.
	 */
	boolean isPauseable();

	/**
	 * Suspend the currently running process. Has no effect if {@link #isPauseable()} return false.
	 */
	void pause();

	/**
	 * Resume a previously paused process. Has no effect if {@link #isPauseable()} return false.
	 */
	void resume();

	/**
	 * Returns the current state of the job.
	 * 
	 * @return
	 */
	EJobState getJobState();

	/**
	 * Returns progress as percentage [0:100]
	 * 
	 * @return
	 */
	int getProgress();

	/**
	 * Returns the current job output or null if there is nothing to report. This may be the final or an intermediate result state or some other arbitrary information. F
	 * 
	 * @return
	 */
	Object getJobOutput();
	

	/**
	 * Add a listener for {@link IJobControl} instances to track changes in state and progress.
	 * 
	 * @param listener
	 */
	void addListener(@NonNull IJobControlListener listener);

	/**
	 * Remove a previous registered listener
	 * 
	 * @param listener
	 */
	void removeListener(@NonNull IJobControlListener listener);

	/**
	 * {@link #cancel()} any running optimisation and clean up references.
	 */
	void dispose();
}
