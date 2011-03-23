package com.mmxlabs.rcp.navigator.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.HandlerEvent;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IResource;

import com.mmxlabs.jobcontoller.Activator;
import com.mmxlabs.jobcontroller.core.IJobManager;
import com.mmxlabs.jobcontroller.core.IJobManagerListener;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.jobcontroller.core.IManagedJob.JobState;
import com.mmxlabs.jobcontroller.core.IManagedJobListener;

/**
 * Base {@link IHandler} implementation to cause enabled state to be refreshed
 * on job progress updates.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public abstract class AbstractOptimisationHandler extends AbstractHandler {

	/**
	 * {@link IManagedJobListener} to trigger event handler enabled state
	 * refresh
	 */
	private final IManagedJobListener jobListener = new IManagedJobListener() {

		@Override
		public void jobStateChanged(final IManagedJob job,
				final JobState oldState, final JobState newState) {

			// Create Event to trigger enabled state update
			final HandlerEvent event = new HandlerEvent(
					AbstractOptimisationHandler.this, true, false);

			// Fire the event
			fireHandlerChanged(event);
		}

		@Override
		public void jobProgressUpdated(final IManagedJob job,
				final int progressDelta) {

		}
	};

	/**
	 * {@link IJobManagerListener} to hook up our {@link #jobListener} to any
	 * jobs added to the manager, and remove the listener as the job is removed.
	 */
	final IJobManagerListener jobManagerListener = new IJobManagerListener() {

		@Override
		public void jobAdded(final IJobManager jobManager,
				final IManagedJob job, final IResource resource) {
			job.addListener(jobListener);
		}

		@Override
		public void jobRemoved(final IJobManager jobManager,
				final IManagedJob job, final IResource resource) {

			job.removeListener(jobListener);
		}

		@Override
		public void jobSelected(final IJobManager jobManager,
				final IManagedJob job, final IResource resource) {

		}

		@Override
		public void jobDeselected(final IJobManager jobManager,
				final IManagedJob job, final IResource resource) {

		}

	};

	/**
	 * The constructor.
	 */
	public AbstractOptimisationHandler() {
		final IJobManager jmv = Activator.getDefault().getJobManager();
		// Hook in a listener to automatically dispose the job once it is no
		// longer needed
		jmv.addJobManagerListener(jobManagerListener);

		// Hook listener in to current jobs
		for (final IManagedJob j : jmv.getJobs()) {
			j.addListener(jobListener);
		}
	}

	@Override
	public void dispose() {

		final IJobManager jmv = Activator.getDefault().getJobManager();

		jmv.removeJobManagerListener(jobManagerListener);

		for (final IManagedJob j : jmv.getJobs()) {
			j.removeListener(jobListener);
		}
		super.dispose();
	}
}
