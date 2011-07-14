/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.ui.jobmanager.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.jobcontroller.core.IJobManager;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.lngscheduler.ui.Activator;
import com.mmxlabs.lngscheduler.ui.LNGSchedulerJob;
import com.mmxlabs.lngscheduler.ui.SaveJobUtil;
import com.mmxlabs.lngscheduler.ui.navigator.handlers.AbstractOptimisationHandler;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SaveOptimisationJobHandler extends AbstractOptimisationHandler {

	/**
	 * The constructor.
	 */
	public SaveOptimisationJobHandler() {

	}

	/**
	 * the command has been executed, so extract extract the needed information from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		IJobManager jobManager = Activator.getDefault().getJobManager();

		if (selection != null && selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();

				if (obj instanceof LNGSchedulerJob) {

					final LNGSchedulerJob job = (LNGSchedulerJob) obj;
					final IResource resource = jobManager.findResourceForJob(job);

					final IWorkspaceRunnable runnable = new IWorkspaceRunnable() {

						@Override
						public void run(final IProgressMonitor monitor) throws CoreException {
							monitor.beginTask("Save Scenario", 2);
							try {
								SaveJobUtil.saveLNGSchedulerJob(job, resource);
								if (resource != null) {
									resource.getParent().refreshLocal(IResource.DEPTH_ONE, new SubProgressMonitor(monitor, 1));
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

		// Update button state?

		return null;
	}

//	@Override
//	public boolean isEnabled() {
//
//		// We could do some of this in plugin.xml - but not been able to
//		// configure it properly.
//		// Plugin.xml will make it enabled if the resource can be a Scenario.
//		// But need finer grained control depending on optimisation state.
//		if (!super.isEnabled()) {
//			return false;
//		}
//
//		final IWorkbench workbench = PlatformUI.getWorkbench();
//		if (workbench == null) {
//			return false;
//		}
//		final IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
//		if (activeWorkbenchWindow == null) {
//			return false;
//		}
//		final ISelection selection = activeWorkbenchWindow.getSelectionService().getSelection();
//
//		if (selection != null && selection instanceof IStructuredSelection) {
//			final IStructuredSelection strucSelection = (IStructuredSelection) selection;
//
//			final Iterator<?> itr = strucSelection.iterator();
//			while (itr.hasNext()) {
//				final Object obj = itr.next();
//				return (obj instanceof LNGSchedulerJob);
//			}
//		}
//
//		return false;
//	}
}
