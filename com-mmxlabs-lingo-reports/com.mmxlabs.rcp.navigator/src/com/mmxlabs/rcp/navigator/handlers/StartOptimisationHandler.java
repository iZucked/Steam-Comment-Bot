/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.rcp.navigator.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import scenario.Scenario;

import com.mmxlabs.jobcontoller.Activator;
import com.mmxlabs.jobcontroller.core.IJobManager;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.jobcontroller.core.IManagedJob.JobState;
import com.mmxlabs.jobcontroller.core.impl.DisposeOnRemoveListener;
import com.mmxlabs.jobcontroller.core.impl.LNGSchedulerJob;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class StartOptimisationHandler extends AbstractOptimisationHandler {

	/**
	 * The constructor.
	 */
	public StartOptimisationHandler() {

	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final IJobManager jmv = Activator.getDefault().getJobManager();

		final ISelection selection = HandlerUtil
				.getActiveWorkbenchWindow(event).getActivePage().getSelection();

		if (selection != null && selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();

				if (obj instanceof IResource) {
					final IResource resource = (IResource) obj;
					final Scenario s = (Scenario) resource
							.getAdapter(Scenario.class);

					if (s == null) {
						return false;
					}

					// Find pre-existing job
					IManagedJob job = Activator.getDefault().getJobManager()
							.findJobForResource(resource);

					// If there is a job, but it is terminated, then we need to
					// create a new one
					if (job != null
							&& (job.getJobState() == JobState.CANCELLED || job
									.getJobState() == JobState.COMPLETED)) {

						// Remove from job handler -- this should trigger
						// the listener registered on creation;
						jmv.removeJob(job);

						// Remove this reference
						job = null;
					}

					// Check for useable pre-existing job?
					if (job == null) {
						job = createOptimisationJob(jmv, resource, s);
					}

					if (job.getJobState() == JobState.CREATED) {
						job.prepare();
						job.start();
						// Resume if paused
					} else if (job.getJobState() == JobState.PAUSED) {
						job.resume();
					} else {
						job.start();
					}
				}
			}
		}

		return null;
	}

	private IManagedJob createOptimisationJob(final IJobManager jmv,
			final IResource resource, final Scenario scenario) {

		final LNGSchedulerJob newJob = new LNGSchedulerJob(scenario);
		jmv.addJob(newJob, resource);

		// Hook in a listener to automatically dispose the job once it is no
		// longer needed
		jmv.addJobManagerListener(new DisposeOnRemoveListener(newJob));

		final Job eclipseJob = new Job("Evaluate initial state of "
				+ scenario.getName()) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				newJob.prepare();
				return Status.OK_STATUS;
			}
		};

		eclipseJob.setPriority(Job.SHORT);
		eclipseJob.schedule();
		
//		newJob.prepare();

		return newJob;
	}

	@Override
	public boolean isEnabled() {

		// We could do some of this in plugin.xml - but not been able to
		// configure it properly.
		// Plugin.xml will make it enabled if the resource can be a Scenario.
		// But need finer grained control depending on optimisation state.
		if (!super.isEnabled()) {
			return false;
		}

		final ISelection selection = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getSelectionService()
				.getSelection();

		if (selection != null && selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			// if
			// (id.equals("com.mmxlabs.rcp.navigator.commands.optimisation.play"))
			// {
			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();

				if (obj instanceof IResource) {
					final IResource resource = (IResource) obj;
					final Scenario s = (Scenario) resource
							.getAdapter(Scenario.class);

					// Need a scenario to start an optimisation
					if (s == null) {
						return false;
					}

					final IManagedJob job = Activator.getDefault()
							.getJobManager().findJobForResource(resource);

					if (job == null) {
						return true;
					}

					return (job.getJobState() != JobState.RUNNING && job
							.getJobState() != JobState.CANCELLING);
				}
			}
		}

		return false;
	}
}
