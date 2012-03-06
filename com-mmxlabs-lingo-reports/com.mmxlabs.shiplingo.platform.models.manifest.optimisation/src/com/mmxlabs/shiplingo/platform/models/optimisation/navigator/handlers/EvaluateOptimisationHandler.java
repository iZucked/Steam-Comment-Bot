/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.optimisation.navigator.handlers;

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
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.shiplingo.platform.models.optimisation.Activator;
import com.mmxlabs.shiplingo.platform.models.optimisation.LNGSchedulerJobControl;
import com.mmxlabs.shiplingo.platform.models.optimisation.LNGSchedulerJobDescriptor;

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
		final IEclipseJobManager jm = Activator.getDefault().getJobManager();

		if ((selection != null) && (selection instanceof IStructuredSelection)) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();

				if (obj instanceof IResource) {
					final IResource resource = (IResource) obj;
					final MMXRootObject scenario = (MMXRootObject) resource.getAdapter(MMXRootObject.class);

					// Need a scenario to start an optimisation
					if (scenario == null) {
						return false;
					}

					final IJobDescriptor existingJob = jm.findJobForResource(resource);
					final IJobControl control = jm.getControlForJob(existingJob);

					if ((control != null) && (control.getJobState() == EJobState.CREATED)) {
						// Remove existing job. We assume that it will be disposed somehow.....
						jm.removeJob(existingJob);
						// } else {
						// return false;
					}

					final WorkspaceJob job = new WorkspaceJob("Evaluate Scenario") {

						@Override
						public IStatus runInWorkspace(final IProgressMonitor monitor) throws CoreException {

							monitor.beginTask("Evaluate Scenario", 5);
							LNGSchedulerJobDescriptor newJob = null;
							try {
								newJob = new LNGSchedulerJobDescriptor(scenario.getName(), scenario);
								final LNGSchedulerJobControl control = new LNGSchedulerJobControl(newJob);

								// Prepare should load up the model and evaluate the initial solution.
								control.prepare();

								final MMXRootObject output = control.getJobOutput().scenario;

								final Iterator<Schedule> iterator = output.getScheduleModel().getSchedules().iterator();
								Schedule lastSchedule = null;
								while (iterator.hasNext()) {
									lastSchedule = iterator.next();
									if (iterator.hasNext()) {
										iterator.remove();
									}
								}
								output.getOptimisation().getCurrentSettings().setInitialSchedule(lastSchedule);

								// Create new resource using original scenario URI
								final XMIResourceImpl r = new XMIResourceImpl(scenario.eResource().getURI());
								// Copy scenario to ensure we don't change resources.
								r.getContents().add(EcoreUtil.copy(output));
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

		if ((selection != null) && (selection instanceof IStructuredSelection)) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			// if

			// (id.equals("com.mmxlabs.rcp.navigator.commands.optimisation.play"))
			// {

			final IEclipseJobManager jm = Activator.getDefault().getJobManager();
			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();

				if (obj instanceof IResource) {
					final IResource resource = (IResource) obj;
					final MMXRootObject s = (MMXRootObject) resource.getAdapter(MMXRootObject.class);

					// Need a scenario to start an optimisation
					if (s == null) {
						return false;
					}

					final IJobDescriptor job = jm.findJobForResource(resource);
					final IJobControl control = jm.getControlForJob(job);
					if ((job == null) || (control.getJobState() == EJobState.CREATED)) {
						return true;
					}
				}
			}
		}

		return false;
	}
}
