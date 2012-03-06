/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.manifest.optimisation.navigator.scenario;

import java.util.Collections;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;

import scenario.Scenario;
import scenario.schedule.Schedule;
import scenario.schedule.ScheduleModel;

import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.lngscheduler.ui.Activator;
import com.mmxlabs.rcp.common.ecore.EcoreContentProvider;

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
