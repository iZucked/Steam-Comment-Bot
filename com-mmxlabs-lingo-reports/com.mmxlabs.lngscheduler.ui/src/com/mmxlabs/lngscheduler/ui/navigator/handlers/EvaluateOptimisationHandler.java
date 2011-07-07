/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.ui.navigator.handlers;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import scenario.Scenario;

import com.mmxlabs.jobcontoller.Activator;
import com.mmxlabs.jobcontroller.core.IJobManager;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.jobcontroller.core.IManagedJob.JobState;
import com.mmxlabs.lngscheduler.ui.LNGSchedulerJob;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class EvaluateOptimisationHandler extends AbstractOptimisationHandler {

	/**
	 * The constructor.
	 */
	public EvaluateOptimisationHandler() {

	}

	/**
	 * the command has been executed, so extract extract the needed information from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		final IJobManager jobManager = Activator.getDefault().getJobManager();

		if (selection != null && selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();

				if (obj instanceof IResource) {
					final IResource resource = (IResource) obj;
					final Scenario scenario = (Scenario) resource.getAdapter(Scenario.class);

					// Need a scenario to start an optimisation
					if (scenario == null) {
						return false;
					}

					final IManagedJob existingJob = jobManager.findJobForResource(resource);

					if (existingJob == null || existingJob.getJobState() == JobState.CREATED) {
						// Remove existing job. We assume that it will be disposed somehow.....
						jobManager.removeJob(existingJob);
					} else {
						return false;
					}

					final WorkspaceJob job = new WorkspaceJob("Evaluate Scenario") {

						@Override
						public IStatus runInWorkspace(final IProgressMonitor monitor) throws CoreException {

							monitor.beginTask("Evaluate Scenario", 5);
							LNGSchedulerJob newJob = null;
							try {
								newJob = new LNGSchedulerJob(scenario);
								newJob.prepare();

								final Scenario newS = newJob.getScenario();

								// Process scenario - prune out intermediate schedules ....
								int numSchedules = newS.getScheduleModel().getSchedules().size();
								while (numSchedules > 1) {
									newS.getScheduleModel().getSchedules().remove(0);
									--numSchedules;
								}
								// .. and set remaining schedule to the new initial state
								if (numSchedules == 1) {
									newS.getOptimisation().getCurrentSettings().setInitialSchedule(newS.getScheduleModel().getSchedules().get(0));
								} else {
									// TODO: Necessary?
									// newS.getOptimisation().getCurrentSettings().setInitialSchedule(null);
								}

								// Create new resource using original scenario URI
								final XMIResourceImpl r = new XMIResourceImpl(scenario.eResource().getURI());
								r.getContents().add(newJob.getScenario());
								try {
									r.save(Collections.emptyMap());
									monitor.worked(4);
								} catch (final IOException e) {
									e.printStackTrace();
								}

								resource.refreshLocal(IResource.DEPTH_ONE, new SubProgressMonitor(monitor, 1));
							} finally {
								monitor.done();
								if (newJob != null) {
									newJob.dispose();
								}
							}

							return Status.OK_STATUS;
						}
					};

					// Trigger UI for progress monitor
					job.setUser(true);
					// Block other changes to this resource
					job.setRule(resource);
					// Schedule job for launching
					job.schedule();

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
					final Scenario s = (Scenario) resource.getAdapter(Scenario.class);

					// Need a scenario to start an optimisation
					if (s == null) {
						return false;
					}

					final IManagedJob job = Activator.getDefault().getJobManager().findJobForResource(resource);

					if (job == null || job.getJobState() == JobState.CREATED) {
						return true;
					}
				}
			}
		}

		return false;
	}
}
