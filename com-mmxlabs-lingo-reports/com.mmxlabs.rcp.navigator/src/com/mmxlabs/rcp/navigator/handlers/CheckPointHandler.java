package com.mmxlabs.rcp.navigator.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.rcp.navigator.scenario.ScenarioTreeNodeClass;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class CheckPointHandler extends AbstractHandler {

	/**
	 * The constructor.
	 */
	public CheckPointHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final ISelection selection = HandlerUtil
				.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		if (selection != null & selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();

				if (obj instanceof ScenarioTreeNodeClass) {
					final ScenarioTreeNodeClass node = (ScenarioTreeNodeClass) obj;

					final WorkspaceJob job = new WorkspaceJob("Checkpoint") {

						@Override
						public IStatus runInWorkspace(final IProgressMonitor monitor)
								throws CoreException {
							
							monitor.beginTask("Copy Scenario", 3);
							try {

								final IContainer container = node.getContainer();
								final IResource resource = node.getResource();

								// Obtain a handle for the new folder
								final IFolder folder = ((IFolder) container)
										.getFolder(container.getName());
								
								// Create the new folder
								folder.create(true, true,
										new SubProgressMonitor(monitor, 1));
								
								// Copy the scenario file into new folder
								resource.copy(
										folder.getFullPath().append(
												"/.scenario"), true,
										new SubProgressMonitor(monitor, 1));

								// Trigger refresh (although a workspace job might do this already?
								container.refreshLocal(
										IResource.DEPTH_INFINITE,
										new SubProgressMonitor(monitor, 1));
								
							} finally {
								monitor.done();
							}
							return Status.OK_STATUS;
						}
					};
					
					// Block other modifications to container 
					// TODO: Should this be the resource instead/aswell?
//					job.setRule(node.getContainer());
					
					job.setRule(MultiRule.combine(node.getContainer(), node.getResource()));
					
					// Run the job as soon as possible
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

		final ISelection selection = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getSelectionService()
				.getSelection();

		if (selection != null & selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();
				return (obj instanceof ScenarioTreeNodeClass);
			}
		}

		return false;
	}
}
