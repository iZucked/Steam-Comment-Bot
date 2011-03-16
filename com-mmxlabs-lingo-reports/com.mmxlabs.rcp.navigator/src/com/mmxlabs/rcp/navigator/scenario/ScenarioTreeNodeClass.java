package com.mmxlabs.rcp.navigator.scenario;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;

import scenario.Scenario;
import scenario.schedule.Schedule;

import com.mmxlabs.jobcontroller.core.IManagedJob;

public class ScenarioTreeNodeClass implements IAdaptable {

	private IContainer container;

	private IResource resource;

	private Scenario scenario;
	
	private IManagedJob job;

	public final IContainer getContainer() {
		return container;
	}

	public final void setContainer(final IContainer container) {
		this.container = container;
	}

	public final Scenario getScenario() {
		return scenario;
	}

	public final void setScenario(final Scenario scenario) {
		this.scenario = scenario;
	}

	@Override
	public Object getAdapter(final Class adapter) {
		// TODO Auto-generated method stub

		if (Schedule.class.isAssignableFrom(adapter)) {
			if (scenario != null) {
				if (scenario.getScheduleModel() != null) {
					EList<Schedule> list = scenario.getScheduleModel().getSchedules();
					if (!list.isEmpty()) {
						return list.get(list.size() - 1);
					}
				}
			}
		}

		else if (Scenario.class.isAssignableFrom(adapter)) {
			return scenario;
		}

		else if (IContainer.class.isAssignableFrom(adapter)) {
			return container;
		}

		else if (IResource.class.isAssignableFrom(adapter)) {
			return resource;
		}

		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	public void setResource(final IResource resource) {
		this.resource = resource;

	}

	public final IResource getResource() {
		return resource;
	}
	
	public final IManagedJob getJob() {
		return job;
	}

	public final void setJob(IManagedJob job) {
		this.job = job;
	}
}
