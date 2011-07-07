/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.ui.navigator.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
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
	 * the command has been executed, so extract extract the needed information from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		IJobManager jobManager = Activator.getDefault().getJobManager();

		final ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();

		if (selection != null && selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();

				if (obj instanceof IResource) {
					final IResource resource = (IResource) obj;

					// Adapt to a new or existing job
					IManagedJob job = (IManagedJob) resource.getAdapter(IManagedJob.class);
					
					// No job - then unable to adapt or wrong type of resource
					if (job == null) {
						// Not a lot we can do here....
						return null;
					}

					// If there is a job, but it is terminated, then we need to create a new one
					if (job != null && (job.getJobState() == JobState.CANCELLED || job.getJobState() == JobState.COMPLETED)) {

						// Remove from job handler -- this should trigger the dispose listener registered on creation.
						jobManager.removeJob(job);

						// Create a new instance - this should use the default job manager
						job = (IManagedJob) resource.getAdapter(IManagedJob.class);
					}

					// If the job does not already exist - it may do perhaps due to a race condition as did not exist when we started this code branch - then register it
					if (!jobManager.getSelectedJobs().contains(job)) {
						// Clean up when job is removed from manager
						jobManager.addJobManagerListener(new DisposeOnRemoveListener(job));
						jobManager.addJob(job, resource);
					}


					// Now look to see whether or not we need to prepare, start or resume
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

	@Override
	public boolean isEnabled() {

		// We could do some of this in plugin.xml - but not been able to
		// configure it properly.
		// Plugin.xml will make it enabled if the resource can be a Scenario.
		// But need finer grained control depending on optimisation state.
		if (!super.isEnabled()) {
			return false;
		}

		final ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();

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
					// FIXME: Make this work efficently without a scenario
					final Scenario s = (Scenario) resource.getAdapter(Scenario.class);

					// Need a scenario to start an optimisation
					if (s == null) {
						return false;
					}

					final IManagedJob job = Activator.getDefault().getJobManager().findJobForResource(resource);

					if (job == null) {
						return true;
					}

					return (job.getJobState() != JobState.RUNNING && job.getJobState() != JobState.CANCELLING);
				}
			}
		}

		return false;
	}
}
