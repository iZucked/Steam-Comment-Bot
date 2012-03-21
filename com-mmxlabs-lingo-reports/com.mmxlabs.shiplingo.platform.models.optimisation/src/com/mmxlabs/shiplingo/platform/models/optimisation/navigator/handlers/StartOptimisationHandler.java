/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.optimisation.navigator.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.validation.marker.MarkerUtil;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.eclipse.manager.impl.DisposeOnRemoveEclipseListener;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.jointmodel.JointModel;
import com.mmxlabs.shiplingo.platform.models.optimisation.Activator;

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

		final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();

		final ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();

		if ((selection != null) && (selection instanceof IStructuredSelection)) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();

				if (obj instanceof IResource) {
					final IResource resource = (IResource) obj;

					final IStatus status = (IStatus) resource.getAdapter(IStatus.class);
					if (status.matches(IStatus.ERROR)) {
						Platform.getLog(Activator.getDefault().getBundle()).log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Validation errors were found in the resource " + resource.getName()));
						try {
							final MMXRootObject root = (MMXRootObject) resource.getAdapter(MMXRootObject.class);
							final ResourceSet rs = root.eResource().getResourceSet();
							// temporarily mess with joint model resource set's uri converter
							// so that all of the URIs appear to be the same, for marker util.
							
							// this may need improving later.
							final URIConverter temp = rs.getURIConverter();
							rs.setURIConverter(new ExtensibleURIConverterImpl() {
								@Override
								public URI normalize(URI uri) {
									return URI.createPlatformResourceURI(resource.getFullPath().toString(), true);
								}
							});
							MarkerUtil.updateMarkers(status);
							
							rs.setURIConverter(temp);
							PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("org.eclipse.ui.views.ProblemView" // TODO find where this lives
									, null, IWorkbenchPage.VIEW_VISIBLE);
						} catch (final CoreException e) {
							Platform.getLog(Activator.getDefault().getBundle()).log(
									new Status(IStatus.ERROR, Activator.PLUGIN_ID, "An error occurred when creating validtion markers for an invalid scenario", e));
						}

						return null;
					}

					// Adapt to a new or existing job
					IJobDescriptor job = (IJobDescriptor) resource.getAdapter(IJobDescriptor.class);

					// No job - then unable to adapt or wrong type of resource
					if (job == null) {
						// Not a lot we can do here....
						return null;
					}

					IJobControl control = jobManager.getControlForJob(job);

					// If there is a job, but it is terminated, then we need to create a new one
					if ((control != null) && ((control.getJobState() == EJobState.CANCELLED) || (control.getJobState() == EJobState.COMPLETED))) {

						// Remove from job handler -- this should trigger the dispose listener registered on creation.
						jobManager.removeJob(job);

						// Create a new instance - this should use the default job manager
						job = (IJobDescriptor) resource.getAdapter(IJobDescriptor.class);
					}

					// If the job does not already exist - it may do perhaps due to a race condition as did not exist when we started this code branch - then register it
					if (!jobManager.getSelectedJobs().contains(job)) {
						// Clean up when job is removed from manager
						jobManager.addEclipseJobManagerListener(new DisposeOnRemoveEclipseListener(job));
						control = jobManager.submitJob(job, resource);
					}

					// Now look to see whether or not we need to prepare, start or resume
					if (control.getJobState() == EJobState.CREATED) {
						try {
							control.prepare();
							control.start();
						} catch (final Exception ex) {
							Platform.getLog(Activator.getDefault().getBundle()).log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.OK, "An error ocurred starting the optimisation", ex));
							control.cancel();
						}
						// Resume if paused
					} else if (control.getJobState() == EJobState.PAUSED) {
						control.resume();
					} else {
						control.start();
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

		if ((selection != null) && (selection instanceof IStructuredSelection)) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();

				if (obj instanceof IResource) {
					final IResource resource = (IResource) obj;
					// FIXME: Make this work efficiently without a scenario
					final MMXRootObject s = (MMXRootObject) resource.getAdapter(MMXRootObject.class);

					// Need a scenario to start an optimisation
					if (s == null) {
						return false;
					}

					final IJobDescriptor job = jobManager.findJobForResource(resource);
					final IJobControl control = jobManager.getControlForJob(job);

					if (control == null) {
						return true;
					}

					return ((control.getJobState() != EJobState.RUNNING) && (control.getJobState() != EJobState.CANCELLING));
				}
			}
		}

		return false;
	}
}
