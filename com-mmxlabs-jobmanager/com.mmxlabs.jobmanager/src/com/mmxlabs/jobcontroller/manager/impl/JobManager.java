/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobcontroller.manager.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IResource;

import com.mmxlabs.jobcontroller.jobs.IManagedJob;
import com.mmxlabs.jobcontroller.manager.IJobManager;
import com.mmxlabs.jobcontroller.manager.IJobManagerListener;

/**
 * @author Simon Goodall
 * 
 */
public final class JobManager implements IJobManager {

	private final List<IManagedJob> jobs = new LinkedList<IManagedJob>();

	private final Map<IManagedJob, IResource> jobResourceMap = new HashMap<IManagedJob, IResource>();

	private final List<IManagedJob> selectedJobs = new LinkedList<IManagedJob>();

	private final List<IResource> selectedResources = new LinkedList<IResource>();

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
	public void addJob(final IManagedJob job, final IResource resource) {
		jobs.add(job);
		final IResource oldResource = jobResourceMap.put(job, resource);
		if (oldResource != null) {
			throw new IllegalStateException(
					"Job already exists - state is now inconsistent!");
		}

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
		if (job == null) return;
		if (selectedJobs.contains(job)) {
			deselectJob(job);
		}

		jobs.remove(job);

		fireJobRemoved(job);
		jobResourceMap.remove(job);
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
		final IResource resource = jobResourceMap.get(job);

		// Take a copy of the set before iterating over it as it is possible
		// that the listeners may be changed as a results of the event
		final Set<IJobManagerListener> local_jobManagerListeners = new HashSet<IJobManagerListener>(
				jobManagerListeners);

		for (final IJobManagerListener l : local_jobManagerListeners) {
			l.jobAdded(this, job, resource);
		}
	}

	/**
	 * @param job
	 */
	private void fireJobRemoved(final IManagedJob job) {

		final IResource resource = jobResourceMap.get(job);

		// Take a copy of the set before iterating over it as it is possible
		// that the listeners may be changed as a results of the event
		final Set<IJobManagerListener> local_jobManagerListeners = new HashSet<IJobManagerListener>(
				jobManagerListeners);

		for (final IJobManagerListener l : local_jobManagerListeners) {
			l.jobRemoved(this, job, resource);
		}
	}

	/**
	 * @param job
	 */
	private void fireJobSelected(final IManagedJob job) {
		final IResource resource = jobResourceMap.get(job);

		// Take a copy of the set before iterating over it as it is possible
		// that the listeners may be changed as a results of the event
		final Set<IJobManagerListener> local_jobManagerListeners = new HashSet<IJobManagerListener>(
				jobManagerListeners);

		for (final IJobManagerListener l : local_jobManagerListeners) {
			l.jobSelected(this, job, resource);
		}
	}

	/**
	 * @param job
	 */
	private void fireJobDeselected(final IManagedJob job) {
		final IResource resource = jobResourceMap.get(job);

		// Take a copy of the set before iterating over it as it is possible
		// that the listeners may be changed as a results of the event
		final Set<IJobManagerListener> local_jobManagerListeners = new HashSet<IJobManagerListener>(
				jobManagerListeners);

		for (final IJobManagerListener l : local_jobManagerListeners) {
			l.jobDeselected(this, job, resource);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.jobcontroller.core.IJobManager#getSelectedJobs()
	 */
	@Override
	public List<IManagedJob> getSelectedJobs() {
		return selectedJobs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.jobcontroller.core.IJobManager#toggleJobSelection(com.mmxlabs
	 * .jobcontroller.core.IManagedJob)
	 */
	@Override
	public void toggleJobSelection(final IManagedJob job) {

		if (selectedJobs.contains(job)) {
			deselectJob(job);
		} else {
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
		selectedResources.add(jobResourceMap.get(job));
		fireJobSelected(job);
	}

	/**
	 * Remove job from the list of selected jobs and fire an event.
	 * 
	 * @param job
	 */
	private void deselectJob(final IManagedJob job) {
		selectedJobs.remove(job);
		selectedResources.remove(jobResourceMap.get(job));
		fireJobDeselected(job);
	}

	/**
	 * Remove job from the list of selected jobs and fire an event.
	 * 
	 * @param job
	 */
	private void deselectResource(final IResource resource) {
		selectedResources.remove(resource);
		final IManagedJob job = findJobForResource(resource);
		selectedJobs.remove(job);
		fireJobDeselected(job);
	}

	/**
	 * Add job from the list of selected jobs and fire an event.
	 * 
	 * @param resource
	 */
	private void selectResource(final IResource resource) {
		selectedResources.add(resource);
		final IManagedJob job = findJobForResource(resource);
		selectedJobs.add(job);
		fireJobSelected(job);
	}

	@Override
	public IManagedJob findJobForResource(final IResource resource) {
		for (final Map.Entry<IManagedJob, IResource> entry : jobResourceMap
				.entrySet()) {
			if (entry.getValue().equals(resource)) {
				return entry.getKey();
			}
		}
		return null;
	}

	@Override
	public IResource findResourceForJob(final IManagedJob job) {
		return jobResourceMap.get(job);
	}

	@Override
	public void toggleResourceSelection(final IResource resource) {
		if (selectedResources.contains(resource)) {
			deselectResource(resource);
		} else {
			selectResource(resource);
		}
	}

	@Override
	public void setJobSelection(final IManagedJob job, final boolean selected) {

		if (selectedJobs.contains(job)) {
			if (!selected) {
				deselectJob(job);
			}
		} else {
			if (selected) {
				selectJob(job);
			}
		}
	}

	@Override
	public void setResourceSelection(final IResource resource,
			final boolean selected) {

		if (selectedResources.contains(resource)) {
			if (!selected) {
				deselectResource(resource);
			}
		} else {
			if (selected) {
				selectResource(resource);
			}
		}

	}

	@Override
	public List<IResource> getSelectedResources() {
		return selectedResources;
	}
}
