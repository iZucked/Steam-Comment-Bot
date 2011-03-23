package com.mmxlabs.rcp.navigator.scenario;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;

import scenario.Scenario;
import scenario.schedule.Schedule;
import scenario.schedule.ScheduleModel;

import com.mmxlabs.rcp.navigator.ecore.EcoreContentProvider;

/**
 * Needed to make Eclipse Core Expressions resolve the adapt method for arbitary
 * objects
 * 
 * @author Simon Goodall
 * 
 */
public class ScenarioTreeNodeClassAdapterFactory implements IAdapterFactory {

	EcoreContentProvider scp = new EcoreContentProvider();

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {

		// if (adaptableObject instanceof ScenarioTreeNodeClass) {
		// ScenarioTreeNodeClass node = (ScenarioTreeNodeClass) adaptableObject;
		// if (Scenario.class.isAssignableFrom(adapterType)) {
		// return node.getScenario();
		// } else if (IResource.class.isAssignableFrom(adapterType)) {
		// return node.getResource();
		// }
		// }

		if (adaptableObject instanceof IResource) {

			if (Scenario.class.isAssignableFrom(adapterType)) {
				Scenario s = getScenario((IResource) adaptableObject);
				return s;
			} else if (Schedule.class.isAssignableFrom(adapterType)) {
				Scenario s = getScenario((IResource) adaptableObject);
				if (s != null) {
					ScheduleModel scheduleModel = s.getScheduleModel();
					if (scheduleModel != null) {
						int size = scheduleModel.getSchedules().size();
						if (size > 0) {
							return scheduleModel.getSchedules().get(size - 1);
						}
					}
				}
			}
		}

		return null;
	}

	Scenario getScenario(IResource resource) {
		final Object[] scenarioObjects = scp.getChildren(resource);
		if (scenarioObjects != null) {
			for (final Object o : scenarioObjects) {
				if (o instanceof Scenario) {
					Scenario scenario = (Scenario) o;
					return scenario;
				}
			}
		}
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] { Scenario.class, IResource.class };
	}
}
