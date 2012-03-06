/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.optimisation.navigator.scenario;

import java.util.Collections;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;

import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.rcp.common.ecore.EcoreContentProvider;
import com.mmxlabs.shiplingo.platform.models.optimisation.Activator;

/**
 * An {@link IAdapterFactory} used to get {@link Schedule} instances from a {@link Scenario} or {@link IResource}. This should be registered in the plugin.xml for this plugin.
 * 
 * @author Simon Goodall
 * 
 */
public class ScenarioTreeNodeClassAdapterFactory implements IAdapterFactory {

	// FIXME: Get this string from somewhere else
	private final EcoreContentProvider scp = new EcoreContentProvider(Collections.singleton("scenario"));

	@Override
	public Object getAdapter(final Object adaptableObject, @SuppressWarnings("rawtypes") final Class adapterType) {

		if (adaptableObject instanceof IResource) {

			/**
			 * Try obtaining in memory data from a running job before falling back to loading the scenario from the resource. This allows the current optimisation state to be shown.
			 */
			final IJobDescriptor job = Activator.getDefault().getJobManager().findJobForResource(adaptableObject);

			// if (job != null) {
			// FIXME
			// final Scenario scenario = ((LNGSchedulerJob) job).getScenario();
			// if (scenario != null) {
			// if (Scenario.class.isAssignableFrom(adapterType)) {
			// return scenario;
			// } else if (Schedule.class.isAssignableFrom(adapterType)) {
			// return getSchedule(scenario);
			// }
			// }
			// }

			// Fall back to directly loading from resource
			if (MMXRootObject.class.isAssignableFrom(adapterType)) {
				final MMXRootObject scenario = getScenario((IResource) adaptableObject);
				return scenario;
			} else if (Schedule.class.isAssignableFrom(adapterType)) {
				final MMXRootObject scenario = getScenario((IResource) adaptableObject);
				return getSchedule(scenario);
			}
		}

		return null;
	}

	private Schedule getSchedule(final MMXRootObject scenario) {
		if (scenario != null) {
			final ScheduleModel scheduleModel = scenario.getSubModel(ScheduleModel.class);
			if (scheduleModel != null) {
				
				if (scheduleModel.getOptimisedSchedule() != null) {
					return scheduleModel.getOptimisedSchedule();
				} else {
					return scheduleModel.getInitialSchedule();
				}
			}
		}
		return null;
	}

	private MMXRootObject getScenario(final IResource resource) {
		final Object[] scenarioObjects = scp.getChildren(resource);
		if (scenarioObjects != null) {
			for (final Object o : scenarioObjects) {
				if (o instanceof MMXRootObject) {
					final MMXRootObject scenario = (MMXRootObject) o;
					return scenario;
				}
			}
		}
		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { MMXRootObject.class, IResource.class };
	}
}
