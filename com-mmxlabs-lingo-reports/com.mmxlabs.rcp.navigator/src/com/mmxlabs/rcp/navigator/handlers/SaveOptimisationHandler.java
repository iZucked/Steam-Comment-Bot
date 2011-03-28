package com.mmxlabs.rcp.navigator.handlers;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import scenario.Scenario;

import com.mmxlabs.jobcontoller.Activator;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.jobcontroller.core.impl.LNGSchedulerJob;

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
				if (obj instanceof IResource) {
					final IResource resource = (IResource) obj;

					final IManagedJob job = Activator.getDefault()
							.getJobManager().findJobForResource(resource);

					if (job instanceof LNGSchedulerJob) {

						final IWorkspaceRunnable runnable = new IWorkspaceRunnable() {

							@Override
							public void run(final IProgressMonitor monitor)
									throws CoreException {
								monitor.beginTask("Save Scenario", 2);

								try {
									// Take copy of scenario
									final Scenario scenario = EcoreUtil
											.copy(((LNGSchedulerJob) job)
													.getScenario());

									// Create resource set to save into
									final ResourceSetImpl resourceSet = new ResourceSetImpl();

									final String fileExtension = resource
											.getFileExtension();
									// Create a new filename with timestamp
									final String newPath = resource
											.getLocation()
											.removeFileExtension().toString()
											+ "-"
											+ new Date().getTime()
											+ "."
											+ fileExtension;
									final URI uri = URI.createFileURI(newPath);

									final Resource nResource = resourceSet
											.createResource(uri);

									// Add copied scenario to this resource
									nResource.getContents().add(scenario);

									final Map<?, ?> options = Collections
											.emptyMap();
									try {
										nResource.save(options);
									} catch (final IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									resource.getParent().refreshLocal(
											IResource.DEPTH_ONE,
											new SubProgressMonitor(monitor, 1));
								} finally {
									monitor.done();
								}
							}
						};
						try {
							ResourcesPlugin.getWorkspace().run(runnable, null);
						} catch (final CoreException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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

		if (selection != null & selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();
				if (obj instanceof IResource) {
					final IResource resource = (IResource) obj;
					final IManagedJob job = Activator.getDefault()
							.getJobManager().findJobForResource(resource);
					return job != null;

				}
			}
		}

		return false;
	}
}
