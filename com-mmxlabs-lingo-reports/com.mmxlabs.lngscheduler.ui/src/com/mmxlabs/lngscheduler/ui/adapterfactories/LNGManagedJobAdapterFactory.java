package com.mmxlabs.lngscheduler.ui.adapterfactories;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;

import scenario.Scenario;

import com.mmxlabs.jobcontoller.Activator;
import com.mmxlabs.jobcontroller.core.IJobManager;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.lngscheduler.ui.LNGSchedulerJob;

/**
 * {@link IAdapterFactory} to convert a {@link IResource} into an {@link IManagedJob} - specifically a {@link LNGSchedulerJob} from a {@link Scenario} model. If a {@link IManagedJob} has previously
 * been created and added to the {@link IJobManager} service instance, that instance will be returned rather than creating a new one. Otherwise a new instance will be created. This will need to be
 * added to a {@link IJobManager} by the caller.
 * 
 * @author Simon Goodall
 * 
 */
public class LNGManagedJobAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(final Object adaptableObject, @SuppressWarnings("rawtypes") final Class adapterType) {

		Scenario scenario = null;
		IResource resource = null;
		if (adaptableObject instanceof IResource) {

			resource = (IResource) adaptableObject;

			IJobManager jobManager = Activator.getDefault().getJobManager();
			/**
			 * Try obtaining in memory data from a running job before falling back to loading the scenario from the resource. This allows the current optimisation state to be shown.
			 */
			final IManagedJob job = jobManager.findJobForResource((IResource) adaptableObject);
			if (job != null) {
				return job;
			}

			scenario = (Scenario) resource.getAdapter(Scenario.class);
		} else if (adaptableObject instanceof Scenario) {

			scenario = (Scenario) scenario;

			// TODO: Find the resource?
			resource = null;
		}
		if (scenario == null) {
			return null;
		}

		return createOptimisationJob(resource, scenario);
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { IManagedJob.class };
	}

	private static IManagedJob createOptimisationJob(final IResource resource, final Scenario scenario) {

		// Get resource name to use as job name
		String name = resource.getName();

		// Strip extension from name
		final String fileExtension = resource.getFileExtension();
		if (fileExtension != null) {
			name = name.replaceAll("." + fileExtension, "");
		}
		// FIXME: Should not change the EMF model just to store the name
		// Set model name so reports can show the right thing in the optimisation column.
		scenario.setName(name);
		// Create the job
		final LNGSchedulerJob newJob = new LNGSchedulerJob(scenario);

		return newJob;
	}

}
