/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.rcp.navigator.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.jobcontoller.Activator;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.jobcontroller.core.IManagedJob.JobState;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class PauseOptimisationHandler extends AbstractOptimisationHandler {

	/**
	 * The constructor.
	 */
	public PauseOptimisationHandler() {

	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final ISelection selection = HandlerUtil
				.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		
		if (selection != null && selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();
				if (obj instanceof IResource) {
					final IResource resource = (IResource) obj;

					final IManagedJob job = Activator.getDefault()
							.getJobManager().findJobForResource(resource);

					if (job != null) {
						if (job.getJobState() == JobState.RUNNING) {
							job.pause();
						}
					}
				}
			}
		}

		// Update button state?

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

					final IManagedJob job = Activator.getDefault()
							.getJobManager().findJobForResource(resource);

					if (job == null) {
						return false;
					}

					return (job.getJobState() == JobState.RUNNING);
				}
			}
		}

		return false;
	}
}
