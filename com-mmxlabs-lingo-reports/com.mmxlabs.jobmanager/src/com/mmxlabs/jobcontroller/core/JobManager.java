/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.jobcontroller.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Simon Goodall
 *
 */
public final class JobManager implements IJobManager {

	private final List<IManagedJob> jobs = new LinkedList<IManagedJob>();

	private final List<IManagedJob> selectedJobs = new LinkedList<IManagedJob>();

	private final Set<IJobManagerListener> jobManagerListeners = new HashSet<IJobManagerListener>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.jobcontroller.core.IJobManager#getJobs()
	 */
	@Override
	public List<IManagedJob> getJobs() {
		return Collections.unmodifiableList(jobs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.jobcontroller.core.IJobManager#addJob(com.mmxlabs.jobcontroller
	 * .core.IManagedJob)
	 */
	@Override
	public void addJob(final IManagedJob job) {
		jobs.add(job);

		fireJobAdded(job);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.jobcontroller.core.IJobManager#removeJob(com.mmxlabs.
	 * jobcontroller.core.IManagedJob)
	 */
	@Override
	public void removeJob(final IManagedJob job) {
		if (selectedJobs.contains(job)) {
			deselectJob(job);
		}

		jobs.remove(job);

		fireJobRemoved(job);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.jobcontroller.core.IJobManager#addJobManagerListener(com.
	 * mmxlabs.jobcontroller.core.IJobManagerListener)
	 */
	@Override
	public void addJobManagerListener(
			final IJobManagerListener jobManagerListener) {
		jobManagerListeners.add(jobManagerListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.jobcontroller.core.IJobManager#removeJobManagerListener(com
	 * .mmxlabs.jobcontroller.core.IJobManagerListener)
	 */
	@Override
	public void removeJobManagerListener(
			final IJobManagerListener jobManagerListener) {
		jobManagerListeners.remove(jobManagerListener);
	}

	/**
	 * @param job
	 */
	private void fireJobAdded(final IManagedJob job) {
		for (final IJobManagerListener l : jobManagerListeners) {
			l.jobAdded(this, job);
		}
	}

	/**
	 * @param job
	 */
	private void fireJobRemoved(final IManagedJob job) {
		for (final IJobManagerListener l : jobManagerListeners) {
			l.jobRemoved(this, job);
		}
	}

	/**
	 * @param job
	 */
	private void fireJobSelected(final IManagedJob job) {
		for (final IJobManagerListener l : jobManagerListeners) {
			l.jobSelected(this, job);
		}
	}

	/**
	 * @param job
	 */
	private void fireJobDeselected(final IManagedJob job) {
		for (final IJobManagerListener l : jobManagerListeners) {
			l.jobDeselected(this, job);
		}
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.jobcontroller.core.IJobManager#getSelectedJobs()
	 */
	@Override
	public List<IManagedJob> getSelectedJobs() {
		return selectedJobs;
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.jobcontroller.core.IJobManager#toggleJobSelection(com.mmxlabs.jobcontroller.core.IManagedJob)
	 */
	@Override
	public void toggleJobSelection(final IManagedJob job) {

		if (selectedJobs.contains(job)) {
			deselectJob(job);
		} else {
			// Ensure only one job is active.
			// TODO: Fix up various views to handle multiple jobs
			while (selectedJobs.isEmpty() == false) {
				deselectJob(selectedJobs.get(0));
			}
			
			selectJob(job);
		}
	}

	/**
	 * Add job to the list of selected jobs and fire an event.
	 * 
	 * @param job
	 */
	private void selectJob(final IManagedJob job) {
		selectedJobs.add(job);
		fireJobSelected(job);
	}

	/**
	 * Remove job from the list of selected jobs and fire an event.
	 * 
	 * @param job
	 */
	private void deselectJob(final IManagedJob job) {
		selectedJobs.remove(job);
		fireJobDeselected(job);
	}
}
