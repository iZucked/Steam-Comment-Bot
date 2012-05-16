package com.mmxlabs.shiplingo.platform.models.optimisation.propertytesters;

import org.eclipse.core.expressions.PropertyTester;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.shiplingo.platform.models.optimisation.Activator;

public class JobStatePropertyTester extends PropertyTester {
	public JobStatePropertyTester() {
	}

	@Override
	public boolean test(final Object receiver, final String property, final Object[] args, final Object expectedValue) {
		if ("jobState".equals(property)) {
			return testJobState(receiver, expectedValue);
		} else if ("canPause".equals(property)) {
			final EJobState state = getJobState(receiver);
			if (state == EJobState.RUNNING) return true;
		} else if ("canPlay".equals(property)) {
			final EJobState state = getJobState(receiver);
			if (state == null) return true;
			switch (state) {
			case CANCELLED:
			case COMPLETED:
			case PAUSED:
			case INITIALISED:
			case CREATED:
				return true;
			default: return false;
			}
		} else if ("canTerminate".equals(property)) {
			final EJobState state = getJobState(receiver);
			if (state == null) return false;
			switch (state) {
			case RUNNING:
			case PAUSED:
				return true;
			default: return false;
			}
		} else if ("hasActiveJob".equals(property)) {
			final EJobState state = getJobState(receiver);
			if (state == null) return false;
			switch (state) {
			case RUNNING:
			case PAUSED:
			case PAUSING:
			case INITIALISED:
			case INITIALISING:
			case CANCELLING:
			case RESUMING:
				return true;
			default: return false;
			}
		}
		
		
		return false;
	}

	private EJobState getJobState(final Object receiver) {
		final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
		if (jobManager != null) {
			if (receiver instanceof ScenarioInstance) {
				final ScenarioInstance instance = (ScenarioInstance) receiver;
				final IJobDescriptor descriptor = jobManager.findJobForResource(instance.getUuid());
				if (descriptor != null) {
					final IJobControl control = jobManager.getControlForJob(descriptor);
					if (control!= null) {
						return control.getJobState();
					}
				}
			}
		}
		return null;
	}
	
	private boolean testJobState(final Object receiver,
			final Object expectedValue) {
		final EJobState state = getJobState(receiver);
		
		if (expectedValue != null && state != null) {
			return expectedValue.toString().equalsIgnoreCase(state.name());
		}
					
		return false;
	}
}
