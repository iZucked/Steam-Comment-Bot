/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobcontroller.manager.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.jobcontroller.jobs.IJobControl;
import com.mmxlabs.jobcontroller.jobs.IJobControlFactory;
import com.mmxlabs.jobcontroller.jobs.IJobDescriptor;
import com.mmxlabs.jobcontroller.manager.IJobManager;
import com.mmxlabs.jobcontroller.manager.IJobManagerDescriptor;
import com.mmxlabs.jobcontroller.manager.IJobManagerListener;

/**
 * Implementation of {@link IJobManager}. This class handles the boiler-plate code for managing jobs and firing {@link IJobManagerListener} events. A {@link IJobControlFactory} instance is required to
 * handle the custom logic of generating {@link IJobControl} for a {@link IJobDescriptor}.
 * 
 * @author Simon Goodall
 */
public final class JobManager implements IJobManager {

	private final IJobManagerDescriptor jobManagerDescriptor;

	/**
	 * Mapping between a {@link IJobDescriptor} and a {@link IJobControl}. A reverse mapping is expected to be present in {@link #controlToDescriptorMap}
	 */
	private final Map<IJobDescriptor, IJobControl> descriptorToControlMap = new HashMap<IJobDescriptor, IJobControl>();

	/**
	 * Mapping between a {@link IJobControl} and a {@link IJobDescriptor}. A reverse mapping is expected to be present in {@link #descriptorToControlMap}
	 */
	private final Map<IJobControl, IJobDescriptor> controlToDescriptorMap = new HashMap<IJobControl, IJobDescriptor>();

	private final Set<IJobManagerListener> jobManagerListeners = new HashSet<IJobManagerListener>();

	/**
	 * Factory used to create {@link IJobControl} once a {@link IJobDescriptor} has been submitted.
	 */
	private final IJobControlFactory jobControlFactory;

	public JobManager(final IJobManagerDescriptor jobManagerDescriptor, final IJobControlFactory jobControlFactory) {
		this.jobManagerDescriptor = jobManagerDescriptor;
		this.jobControlFactory = jobControlFactory;
	}


	@Override
	public final IJobManagerDescriptor getDescriptor() {
		return jobManagerDescriptor;
	}

	
	public IJobControlFactory getJobControlFactory() {
		return jobControlFactory;
	}

	@Override
	public Collection<IJobDescriptor> getJobs() {
		return descriptorToControlMap.keySet();
	}

	@Override
	public void addJobManagerListener(final IJobManagerListener jobManagerListener) {
		jobManagerListeners.add(jobManagerListener);
	}

	@Override
	public void removeJobManagerListener(final IJobManagerListener jobManagerListener) {
		jobManagerListeners.remove(jobManagerListener);
	}

	@Override
	public IJobControl submitJob(final IJobDescriptor job) {
		final IJobControl control = jobControlFactory.createJobControl(job);
		if (control != null) {
			descriptorToControlMap.put(job, control);
			controlToDescriptorMap.put(control, job);

			fireJobAdded(job, control);
		}
		return control;
	}

	@Override
	public IJobControl getJobControl(final IJobDescriptor job) {
		return descriptorToControlMap.get(job);
	}

	@Override
	public void removeJobDescriptor(final IJobDescriptor job) {

		if (descriptorToControlMap.containsKey(job)) {
			// Find job control (if it exists).
			final IJobControl jobControl = descriptorToControlMap.get(job);
			if (jobControl != null) {
				jobControl.cancel();
				controlToDescriptorMap.remove(jobControl);
			}

			descriptorToControlMap.remove(job);

			// TODO: Should we do this before cancelling?
			fireJobRemoved(job, jobControl);

			// Clean up job
			if (jobControl != null) {
				jobControl.dispose();
			}
		}
	}

	/**
	 * @param job
	 */
	private void fireJobAdded(final IJobDescriptor job, final IJobControl control) {

		// Take a copy of the set before iterating over it as it is possible
		// that the listeners may be changed as a results of the event
		final Set<IJobManagerListener> local_jobManagerListeners = new HashSet<IJobManagerListener>(jobManagerListeners);

		for (final IJobManagerListener l : local_jobManagerListeners) {
			l.jobAdded(this, job, control);
		}
	}

	/**
	 * @param job
	 */
	private void fireJobRemoved(final IJobDescriptor job, final IJobControl control) {

		// Take a copy of the set before iterating over it as it is possible
		// that the listeners may be changed as a results of the event
		final Set<IJobManagerListener> local_jobManagerListeners = new HashSet<IJobManagerListener>(jobManagerListeners);

		for (final IJobManagerListener l : local_jobManagerListeners) {
			l.jobRemoved(this, job, control);
		}
	}

}
