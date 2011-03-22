package com.mmxlabs.rcp.navigator.scenario;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;

import scenario.Scenario;
import scenario.schedule.Schedule;

import com.mmxlabs.jobcontroller.core.IManagedJob;

public final class ScenarioTreeNodeClass implements IAdaptable {

	private IResource resource;

	private Scenario scenario;

	private IManagedJob job;

	private final Set<IScenarioTreeNodeListener> listeners = new HashSet<IScenarioTreeNodeListener>();

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") final Class adapter) {

		if (Schedule.class.isAssignableFrom(adapter)) {
			if (scenario != null) {
				if (scenario.getScheduleModel() != null) {
					final EList<Schedule> list = scenario.getScheduleModel().getSchedules();
					if (!list.isEmpty()) {
						return list.get(list.size() - 1);
					}
				}
			}
		}

		else if (Scenario.class.isAssignableFrom(adapter)) {
			return scenario;
		} else if (IResource.class.isAssignableFrom(adapter)) {
			return resource;
		}

		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	public final Scenario getScenario() {
		return scenario;
	}

	public final void setScenario(final Scenario scenario) {

		final Scenario oldScenario = this.scenario;

		this.scenario = scenario;

		notifyScenarioChanged(oldScenario, scenario);
	}

	public void setResource(final IResource resource) {

		final IResource oldResource = this.resource;

		this.resource = resource;

		notifyResourceChanged(oldResource, resource);

	}

	public final IResource getResource() {
		return resource;
	}

	public final IManagedJob getJob() {
		return job;
	}

	public final void setJob(final IManagedJob job) {

		final IManagedJob oldJob = this.job;

		this.job = job;

		notifyJobChanged(oldJob, job);
	}

	private void notifyResourceChanged(final IResource oldResource, final IResource newResource) {
		for (final IScenarioTreeNodeListener l : listeners) {
			l.resourceChanged(this, oldResource, newResource);
		}
	}

	private void notifyScenarioChanged(final Scenario oldScenario, final Scenario newScenario) {
		for (final IScenarioTreeNodeListener l : listeners) {
			l.scenarioChanged(this, oldScenario, newScenario);
		}
	}

	private void notifyJobChanged(final IManagedJob oldJob, final IManagedJob newJob) {
		for (final IScenarioTreeNodeListener l : listeners) {
			l.jobChanged(this, oldJob, newJob);
		}
	}

	public void addListener(final IScenarioTreeNodeListener l) {
		listeners.add(l);
	}

	public void removeListener(final IScenarioTreeNodeListener l) {
		listeners.remove(l);
	}
}
