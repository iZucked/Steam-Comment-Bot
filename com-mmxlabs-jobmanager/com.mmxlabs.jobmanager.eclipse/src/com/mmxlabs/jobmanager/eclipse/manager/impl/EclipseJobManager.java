/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import java.util.SortedSet;
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
		if (job == null) {
			return;
		}

		jobs.remove(job);
		jobResourceMap.remove(job);

		final Object resource = jobResourceMap.get(job);
		final IJobControl control = jobToControls.remove(job);

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
		final Set<IEclipseJobManagerListener> localJobManagerListeners = new HashSet<IEclipseJobManagerListener>(jobManagerListeners);

		for (final IEclipseJobManagerListener l : localJobManagerListeners) {
			l.jobAdded(this, job, jobControl, resource);
		}
	}

	/**
	 * @param job
	 */
	private void fireJobRemoved(final IJobDescriptor job, final IJobControl jobControl, final Object resource) {

		// Take a copy of the set before iterating over it as it is possible
		// that the listeners may be changed as a results of the event
		final Set<IEclipseJobManagerListener> localJobManagerListeners = new HashSet<IEclipseJobManagerListener>(jobManagerListeners);

		for (final IEclipseJobManagerListener l : localJobManagerListeners) {
			l.jobRemoved(this, job, jobControl, resource);
		}
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

	protected IJobManager findJobManager(final IJobDescriptor job) {

		// TODO: Make a GUI wrapper to display sorted results and let user pick

		if (jobManagers.isEmpty()) {
			return null;
		}
		// TODO - SHould check really
		if (jobManagers.size() == 1) {
			return jobManagers.values().iterator().next();
		}

		final SortedSet<MatchResult> results = new TreeSet<MatchResult>();
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
		// Clients can override this method
	}

	protected void restoreState(final IJobManager jobManager) {
		// Clients can override this method
	}

	@Override
	public IJobControl getControlForJob(final IJobDescriptor jobDescriptor) {
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
