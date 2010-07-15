package com.mmxlabs.jobcontroller.core;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
		return jobs;
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

	private void fireJobAdded(final IManagedJob job) {
		for (final IJobManagerListener l : jobManagerListeners) {
			l.jobAdded(this, job);
		}
	}

	private void fireJobRemoved(final IManagedJob job) {
		for (final IJobManagerListener l : jobManagerListeners) {
			l.jobRemoved(this, job);
		}
	}

	private void fireJobSelected(final IManagedJob job) {
		for (final IJobManagerListener l : jobManagerListeners) {
			l.jobSelected(this, job);
		}
	}

	private void fireJobDeselected(final IManagedJob job) {
		for (final IJobManagerListener l : jobManagerListeners) {
			l.jobDeselected(this, job);
		}
	}

	@Override
	public List<IManagedJob> getSelectedJobs() {
		return selectedJobs;
	}

	@Override
	public void toggleJobSelection(final IManagedJob job) {

		if (selectedJobs.contains(job)) {
			selectedJobs.remove(job);
			fireJobDeselected(job);
		} else {
			selectedJobs.add(job);
			fireJobSelected(job);
		}
	}
}
