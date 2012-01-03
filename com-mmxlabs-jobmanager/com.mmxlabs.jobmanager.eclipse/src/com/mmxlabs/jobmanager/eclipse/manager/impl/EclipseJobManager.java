/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.eclipse.manager.impl;


import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManagerListener;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.jobmanager.manager.IJobManager;
import com.mmxlabs.jobmanager.manager.IJobMatcher;
import com.mmxlabs.jobmanager.manager.MatchResult;

/**
 * @author Simon Goodall
 * 
 */
public final class EclipseJobManager implements IEclipseJobManager {

	// Need constructor setter
	private IJobMatcher jobMatcher;

	// Need adder/remover methods
	private final Map<String, IJobManager> jobManagers = new HashMap<String, IJobManager>();

	private final List<IJobDescriptor> jobs = new LinkedList<IJobDescriptor>();
	private final Map<IJobDescriptor, IJobManager> jobToManager = new HashMap<IJobDescriptor, IJobManager>();
	private final Map<IJobDescriptor, IJobControl> jobToControls = new HashMap<IJobDescriptor, IJobControl>();

	private final Map<IJobDescriptor, Object> jobResourceMap = new HashMap<IJobDescriptor, Object>();

	private final List<IJobDescriptor> selectedJobs = new LinkedList<IJobDescriptor>();

	private final List<Object> selectedResources = new LinkedList<Object>();

	private final Set<IEclipseJobManagerListener> jobManagerListeners = new HashSet<IEclipseJobManagerListener>();

	public EclipseJobManager() {
		setJobMatcher(new IJobMatcher() {
			@Override
			public int matchJob(final IJobDescriptor descriptor, final IJobManager jobManager) {
				return jobManager.getJobs().size();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.jobcontroller.core.IJobManager#getJobs()
	 */
	@Override
	public List<IJobDescriptor> getJobs() {
		return Collections.unmodifiableList(jobs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.jobcontroller.core.IJobManager#addJob(com.mmxlabs.jobcontroller .core.IManagedJob)
	 */
	@Override
	public IJobControl submitJob(final IJobDescriptor job, final Object resource) {

		if (jobs.contains(job)) {
			throw new IllegalStateException("Job already submitted!");
		}

		final IJobManager jobManager = findJobManager(job);
		if (jobManager == null) {
			throw new RuntimeException("Unable to match job to a manager");
		}

		final IJobControl control = jobManager.submitJob(job);
		if (control == null) {
			throw new RuntimeException("Error submitting job to manager");
		}

		jobs.add(job);
		jobToControls.put(job, control);
		jobToManager.put(job, jobManager);

		jobResourceMap.put(job, resource);

		fireJobAdded(job, control, resource);

		return control;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.jobcontroller.core.IJobManager#removeJob(com.mmxlabs. jobcontroller.core.IManagedJob)
	 */
	@Override
	public void removeJob(final IJobDescriptor job) {
		if (job == null)
			return;
		if (selectedJobs.contains(job)) {
			deselectJob(job);
		}


		jobs.remove(job);
		jobResourceMap.remove(job);

		final Object resource = jobResourceMap.get(job);
		final IJobControl control = jobToControls.get(job);

		for (final IJobManager manager : jobManagers.values()) {
			if (manager.getJobs().contains(job)) {
				manager.removeJobDescriptor(job);
			}
		}

		fireJobRemoved(job, control, resource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.jobcontroller.core.IJobManager#addJobManagerListener(com. mmxlabs.jobcontroller.core.IJobManagerListener)
	 */
	@Override
	public void addEclipseJobManagerListener(final IEclipseJobManagerListener jobManagerListener) {
		jobManagerListeners.add(jobManagerListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.jobcontroller.core.IJobManager#removeJobManagerListener(com .mmxlabs.jobcontroller.core.IJobManagerListener)
	 */
	@Override
	public void removeEclipseJobManagerListener(final IEclipseJobManagerListener jobManagerListener) {
		jobManagerListeners.remove(jobManagerListener);
	}

	/**
	 * @param job
	 */
	private void fireJobAdded(final IJobDescriptor job, final IJobControl jobControl, final Object resource) {

		// Take a copy of the set before iterating over it as it is possible
		// that the listeners may be changed as a results of the event
		final Set<IEclipseJobManagerListener> local_jobManagerListeners = new HashSet<IEclipseJobManagerListener>(jobManagerListeners);

		for (final IEclipseJobManagerListener l : local_jobManagerListeners) {
			l.jobAdded(this, job, jobControl, resource);
		}
	}

	/**
	 * @param job
	 */
	private void fireJobRemoved(final IJobDescriptor job, final IJobControl jobControl, final Object resource) {

		// Take a copy of the set before iterating over it as it is possible
		// that the listeners may be changed as a results of the event
		final Set<IEclipseJobManagerListener> local_jobManagerListeners = new HashSet<IEclipseJobManagerListener>(jobManagerListeners);

		for (final IEclipseJobManagerListener l : local_jobManagerListeners) {
			l.jobRemoved(this, job, jobControl, resource);
		}
	}

	/**
	 * @param job
	 */
	private void fireJobSelected(final IJobDescriptor job, final IJobControl control, final Object resource) {

		// Take a copy of the set before iterating over it as it is possible
		// that the listeners may be changed as a results of the event
		final Set<IEclipseJobManagerListener> local_jobManagerListeners = new HashSet<IEclipseJobManagerListener>(jobManagerListeners);

		for (final IEclipseJobManagerListener l : local_jobManagerListeners) {
			l.jobSelected(this, job, control, resource);
		}
	}

	/**
	 * @param job
	 */
	private void fireJobDeselected(final IJobDescriptor job, final IJobControl jobControl, final Object resource) {

		// Take a copy of the set before iterating over it as it is possible
		// that the listeners may be changed as a results of the event
		final Set<IEclipseJobManagerListener> local_jobManagerListeners = new HashSet<IEclipseJobManagerListener>(jobManagerListeners);

		for (final IEclipseJobManagerListener l : local_jobManagerListeners) {
			l.jobDeselected(this, job, jobControl, resource);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.jobcontroller.core.IJobManager#getSelectedJobs()
	 */
	@Override
	public List<IJobDescriptor> getSelectedJobs() {
		return selectedJobs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.jobcontroller.core.IJobManager#toggleJobSelection(com.mmxlabs .jobcontroller.core.IManagedJob)
	 */
	@Override
	public void toggleJobSelection(final IJobDescriptor job) {

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
	private void selectJob(final IJobDescriptor job) {
		selectedJobs.add(job);
		final Object resource = jobResourceMap.get(job);
		final IJobControl control = jobToControls.get(job);
		selectedResources.add(resource);
		fireJobSelected(job, control, resource);
	}

	/**
	 * Remove job from the list of selected jobs and fire an event.
	 * 
	 * @param job
	 */
	private void deselectJob(final IJobDescriptor job) {
		selectedJobs.remove(job);
		final Object resource = jobResourceMap.get(job);
		final IJobControl control = jobToControls.get(job);
		selectedResources.remove(resource);
		fireJobDeselected(job, control, resource);
	}

	/**
	 * Remove job from the list of selected jobs and fire an event.
	 * 
	 * @param job
	 */
	private void deselectResource(final Object resource) {
		selectedResources.remove(resource);
		final IJobDescriptor job = findJobForResource(resource);
		selectedJobs.remove(job);
		final IJobControl control = jobToControls.get(job);
		fireJobDeselected(job, control, resource);
	}

	/**
	 * Add job from the list of selected jobs and fire an event.
	 * 
	 * @param resource
	 */
	private void selectResource(final Object resource) {
		selectedResources.add(resource);
		final IJobDescriptor job = findJobForResource(resource);
		selectedJobs.add(job);
		final IJobControl control = jobToControls.get(job);
		fireJobSelected(job, control, resource);
	}

	@Override
	public IJobDescriptor findJobForResource(final Object resource) {
		for (final Map.Entry<IJobDescriptor, Object> entry : jobResourceMap.entrySet()) {
			if (entry.getValue().equals(resource)) {
				return entry.getKey();
			}
		}
		return null;
	}

	@Override
	public Object findResourceForJob(final IJobDescriptor job) {
		return jobResourceMap.get(job);
	}

	@Override
	public void toggleResourceSelection(final Object resource) {
		if (selectedResources.contains(resource)) {
			deselectResource(resource);
		} else {
			selectResource(resource);
		}
	}

	@Override
	public void setJobSelection(final IJobDescriptor job, final boolean selected) {

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
	public void setResourceSelection(final Object resource, final boolean selected) {

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
	public List<Object> getSelectedResources() {
		return selectedResources;
	}

	protected IJobManager findJobManager(final IJobDescriptor job) {

		// TODO: Make a GUI wrapper to display sorted results and let user pick

		if (jobManagers.isEmpty()) {
			return null;
		}
		// TODO - SHould check really
		if (jobManagers.size() == 1) {
			return jobManagers.values().iterator().next();
		}
		
		final TreeSet<MatchResult> results = new TreeSet<MatchResult>();
		for (final IJobManager mgr : jobManagers.values()) {
			final int score = jobMatcher.matchJob(job, mgr);
			results.add(new MatchResult(mgr, score));
		}

		return results.first().getManager();
	}

	public void init() {
		// Any init related stuff here.
		if (jobMatcher == null) {
			throw new IllegalStateException("Job Matcher has not been specified");
		}
	}

	public void dispose() {
		jobManagers.clear();
		jobManagerListeners.clear();
		selectedJobs.clear();
		selectedResources.clear();

		jobResourceMap.clear();
		jobs.clear();
		jobToControls.clear();
		jobToManager.clear();

		jobMatcher = null;
	}

	/**
	 * Notify the {@link EclipseJobManager} that an {@link IJobManager} implementation is available to use.
	 * 
	 * @param jobManager
	 */
	public void bind(final IJobManager jobManager) {
		// Store Job Manger reference
		final String name = jobManager.getJobManagerDescriptor().getName();
		jobManagers.put(name, jobManager);

		// Attempt to restore any previous state
		restoreState(jobManager);
	}

	public void unbind(final IJobManager jobManager) {

		// Attempt to save any related job manager state.
		saveState(jobManager);

		final String name = jobManager.getJobManagerDescriptor().getName();
		jobManagers.remove(name);
	}

	protected void saveState(final IJobManager jobManager) {

	}

	protected void restoreState(final IJobManager jobManager) {

	}

	@Override
	public IJobControl getControlForJob(IJobDescriptor jobDescriptor) {
		return jobToControls.get(jobDescriptor);
	}

	public final IJobMatcher getJobMatcher() {
		return jobMatcher;
	}

	public final void setJobMatcher(final IJobMatcher jobMatcher) {
		this.jobMatcher = jobMatcher;
	}

	@Override
	public Collection<IJobManager> getJobManagers() {
		return jobManagers.values();
	}
}
