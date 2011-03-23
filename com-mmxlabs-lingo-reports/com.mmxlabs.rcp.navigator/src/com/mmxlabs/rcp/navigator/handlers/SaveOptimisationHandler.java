package com.mmxlabs.rcp.navigator.handlers;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import scenario.Scenario;

import com.mmxlabs.jobcontoller.Activator;
import com.mmxlabs.jobcontroller.core.IManagedJob;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SaveOptimisationHandler extends AbstractHandler {

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

		final String id = event.getCommand().getId();

		final ISelection selection = HandlerUtil
				.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		if (selection != null & selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			if (id.equals("com.mmxlabs.rcp.navigator.commands.optimisation.save")) {

				final Iterator<?> itr = strucSelection.iterator();
				while (itr.hasNext()) {
					final Object obj = itr.next();
					if (obj instanceof IResource) {
						IResource resource = (IResource)obj;
						Scenario s = (Scenario)resource.getAdapter(Scenario.class);
						if (s == null) {
							return false;
						}
						
						IManagedJob job = Activator.getDefault().getJobManager().findJobForResource(resource);

						ResourceSetImpl resourceSet = new ResourceSetImpl();

						String fileExtension = resource
								.getFileExtension();
						String newPath = resource.getLocation()
								.removeFileExtension().toString()
								+ "-"
								+ new Date().getTime()
								+ "."
								+ fileExtension;
						URI uri = URI.createFileURI(newPath);

						Resource nResource = resourceSet.createResource(uri);
						nResource.getContents().add(s);

						Map<?, ?> options = Collections.emptyMap();
						try {
							nResource.save(options);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// TODO; Really a workspace op
						try {
							resource
									.getParent()
									.refreshLocal(IResource.DEPTH_ONE,
											new NullProgressMonitor());
						} catch (CoreException e) {
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
					IResource resource = (IResource)obj;
					Scenario s = (Scenario)resource.getAdapter(Scenario.class);
					return s != null;
				}
			}
		}

		System.out.println("isEnabled: False");
		return false;
	}
}
