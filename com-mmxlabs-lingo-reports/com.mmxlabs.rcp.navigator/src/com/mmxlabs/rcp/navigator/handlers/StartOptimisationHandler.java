package com.mmxlabs.rcp.navigator.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
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
import com.mmxlabs.jobcontroller.core.IJobManagerListener;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.jobcontroller.core.IManagedJob.JobState;
import com.mmxlabs.jobcontroller.core.impl.LNGSchedulerJob;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class StartOptimisationHandler extends AbstractHandler {

	/**
	 * The constructor.
	 */
	public StartOptimisationHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final String id = event.getCommand().getId();

		final IJobManager jmv = Activator.getDefault().getJobManager();

		final ISelection selection = HandlerUtil
				.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		if (selection != null & selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			if (id.equals("com.mmxlabs.rcp.navigator.commands.optimisation.play")) {

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

						if (job != null && (job.getJobState() == JobState.CANCELLED || job.getJobState() == JobState.COMPLETED)) {
							// Remove from job handler -- this should trigger the listener registered on creation;
							jmv.removeJob(job);
							
							// Remove this reference
							job = null;
						}
						
						// Check for useable pre-existing job?
						if (job == null) {
							job = createOptimisationJob(jmv, resource, s);
						}
						
						if (job.getJobState() == JobState.PAUSED) {
							job.resume();
						} else {
							job.start();
						}
					}
				}
			}
		}

		// Update button state?

		return null;
	}

	private IManagedJob createOptimisationJob(final IJobManager jmv,
			final IResource resource, Scenario scenario) {

		final LNGSchedulerJob newJob = new LNGSchedulerJob(scenario);
		jmv.addJob(newJob, resource);

		// Hook in a listener to automatically dispose the job once it is no longer needed
		jmv.addJobManagerListener(new IJobManagerListener() {
			
			@Override
			public void jobSelected(IJobManager jobManager, IManagedJob job,
					IResource resource) {
				
			}
			
			@Override
			public void jobRemoved(IJobManager jobManager, IManagedJob job,
					IResource resource) {
				
				// If this is the job being removed, then dispose and remove references to it
				if (job == newJob){ 
					newJob.dispose();
					jobManager.removeJobManagerListener(this);
				}
			}
			
			@Override
			public void jobDeselected(IJobManager jobManager, IManagedJob job,
					IResource resource) {
				
			}
			
			@Override
			public void jobAdded(IJobManager jobManager, IManagedJob job,
					IResource resource) {
				
			}
		});

		// TODO: The UI will freeze at this point -- perhaps a Runnable here?
		newJob.prepare();

		
		return newJob;
	}

	@Override
	public boolean isEnabled() {

		// We could do some of this in plugin.xml - but not been able to
		// configure it properly.
		// Plugin.xml will make it enabled if the resource can be a Scenario.
		// But need finer grained control depending on optimisation state.
		if (!super.isEnabled()) {
			System.out.println("isEnabled: False");
			return false;
		}

		final ISelection selection = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getSelectionService()
				.getSelection();

		if (selection != null & selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			// if
			// (id.equals("com.mmxlabs.rcp.navigator.commands.optimisation.play"))
			// {
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
					if (job == null) {
						System.out.println("isEnabled: True");
						return true;
					}

					System.out.println("isEnabled: " + (job.getJobState() != JobState.RUNNING));
					return (job.getJobState() != JobState.RUNNING);
				}
			}
		}

		System.out.println("isEnabled: False");
		return false;
	}
}
