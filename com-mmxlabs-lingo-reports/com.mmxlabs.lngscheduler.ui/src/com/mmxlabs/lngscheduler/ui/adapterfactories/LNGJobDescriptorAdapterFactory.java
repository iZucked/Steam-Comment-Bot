package com.mmxlabs.lngscheduler.ui.adapterfactories;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;

import scenario.Scenario;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.lngscheduler.ui.Activator;
import com.mmxlabs.lngscheduler.ui.LNGSchedulerJobControl;
import com.mmxlabs.lngscheduler.ui.LNGSchedulerJobDescriptor;

/**
 * {@link IAdapterFactory} to convert a {@link LNGSchedulerJobDescriptor} into an {@link IJobControl} - specifically a {@link LNGSchedulerJobControl}.
 * 
 * @author Simon Goodall
 * 
 */
public class LNGJobDescriptorAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(final Object adaptableObject, @SuppressWarnings("rawtypes") final Class adapterType) {
		Scenario scenario = null;
		IResource resource = null;
		if (adaptableObject instanceof IResource) {

			resource = (IResource) adaptableObject;

			IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
			/**
			 * Try obtaining in memory data from a running job before falling back to loading the scenario from the resource. This allows the current optimisation state to be shown.
			 */
			final IJobDescriptor job = jobManager.findJobForResource((IResource) adaptableObject);
			if (job != null) {
				return job;
			}

			scenario = (Scenario) resource.getAdapter(Scenario.class);
//		} else if (adaptableObject instanceof Scenario) {
//
//			scenario = (Scenario) scenario;
//
//			// TODO: Find the resource?
//			resource = null;
		}
		if (scenario == null) {
			return null;
		}

		return createOptimisationJob(resource, scenario);
	}

	private static IJobDescriptor createOptimisationJob(final IResource resource, final Scenario scenario) {

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
		final LNGSchedulerJobDescriptor newJob = new LNGSchedulerJobDescriptor(name, scenario);

		return newJob;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { IJobDescriptor.class };
	}
}
