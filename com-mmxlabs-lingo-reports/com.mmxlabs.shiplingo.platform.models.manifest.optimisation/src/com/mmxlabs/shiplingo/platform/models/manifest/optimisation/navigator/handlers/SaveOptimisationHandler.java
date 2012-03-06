/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.manifest.optimisation.navigator.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.lngscheduler.ui.Activator;
import com.mmxlabs.lngscheduler.ui.LNGSchedulerJobControl;
import com.mmxlabs.lngscheduler.ui.LNGSchedulerJobDescriptor;
import com.mmxlabs.lngscheduler.ui.SaveJobUtil;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SaveOptimisationHandler extends AbstractOptimisationHandler {

	/**
	 * The constructor.
	 */
	public SaveOptimisationHandler() {

	}

	/**
	 * the command has been executed, so extract extract the needed information from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();

		if ((selection != null) && (selection instanceof IStructuredSelection)) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();
				if (obj instanceof IResource) {
					final IResource resource = (IResource) obj;

					final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
					final IJobDescriptor job = jobManager.findJobForResource(resource);
					final IJobControl control = jobManager.getControlForJob(job);

					if (job instanceof LNGSchedulerJobDescriptor) {

						final IWorkspaceRunnable runnable = new IWorkspaceRunnable() {

							@Override
							public void run(final IProgressMonitor monitor) throws CoreException {

								monitor.beginTask("Save Scenario", 2);
								try {
									// Attempt to save job
									// FIXME: Do not hardcode extension
									final IPath path = SaveJobUtil.saveLNGSchedulerJob((LNGSchedulerJobDescriptor) job, (LNGSchedulerJobControl) control, resource.getFileExtension(), resource);
									if (path != null) {
										// An IPath has been returned, try and find the IResource that it corresponds to
										final IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
										if (resource != null) {
											// A resource exists!, refresh the parent so that the navigator will find it.
											resource.getParent().refreshLocal(IResource.DEPTH_ONE, new SubProgressMonitor(monitor, 1));
										}
									}
								} finally {
									monitor.done();
								}
							}
						};
						try {
							ResourcesPlugin.getWorkspace().run(runnable, null);
						} catch (final CoreException e) {
							Activator.error(e.getMessage(), e);
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

		final ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();

		if ((selection != null) && (selection instanceof IStructuredSelection)) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();
				if (obj instanceof IResource) {
					final IResource resource = (IResource) obj;
					final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
					final IJobDescriptor job = jobManager.findJobForResource(resource);
					final IJobControl control = jobManager.getControlForJob(job);

					return (control != null);

				}
			}
		}

		return false;
	}
}
