package com.mmxlabs.rcp.navigator.scenario;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;

import scenario.Scenario;
import scenario.schedule.Schedule;
import scenario.schedule.ScheduleModel;

import com.mmxlabs.jobcontoller.Activator;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.jobcontroller.core.impl.LNGSchedulerJob;
import com.mmxlabs.rcp.navigator.ecore.EcoreContentProvider;

/**
 * Needed to make Eclipse Core Expressions resolve the adapt method for
 * arbitrary objects

 * 
 * TODO: This is no longer used for it's original purpose and should be renamed
 * 
 * @author Simon Goodall
 * 
 */
public class ScenarioTreeNodeClassAdapterFactory implements IAdapterFactory {

	private final EcoreContentProvider scp = new EcoreContentProvider();

	@Override
	public Object getAdapter(final Object adaptableObject,
			@SuppressWarnings("rawtypes") final Class adapterType) {

		if (adaptableObject instanceof IResource) {

			/**
			 * Try obtaining in memory data from a running job before falling
			 * back to loading the scenario from the resource. This allows the
			 * current optimisation state to be shown.
			 */
			final IManagedJob job = Activator.getDefault().getJobManager()
					.findJobForResource((IResource) adaptableObject);
			if (job != null) {
				final Scenario scenario = ((LNGSchedulerJob) job).getScenario();
				if (scenario != null) {
					if (Scenario.class.isAssignableFrom(adapterType)) {
						return scenario;
					} else if (Schedule.class.isAssignableFrom(adapterType)) {
						return getSchedule(scenario);
					}
				}
			}

			// Fall back to directly loading from resource
			if (Scenario.class.isAssignableFrom(adapterType)) {
				final Scenario scenario = getScenario((IResource) adaptableObject);
				return scenario;
			} else if (Schedule.class.isAssignableFrom(adapterType)) {
				final Scenario scenario = getScenario((IResource) adaptableObject);
				return getSchedule(scenario);
			}
		}

		return null;
	}

	private Schedule getSchedule(final Scenario scenario) {
		if (scenario != null) {
			final ScheduleModel scheduleModel = scenario.getScheduleModel();
			if (scheduleModel != null) {
				final int size = scheduleModel.getSchedules().size();
				if (size > 0) {
					return scheduleModel.getSchedules().get(size - 1);
				}
			}
		}
		return null;
	}

	private Scenario getScenario(final IResource resource) {
		final Object[] scenarioObjects = scp.getChildren(resource);
		if (scenarioObjects != null) {
			for (final Object o : scenarioObjects) {
				if (o instanceof Scenario) {
					final Scenario scenario = (Scenario) o;
					return scenario;
				}
			}
		}
		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { Scenario.class, IResource.class };
	}
}
