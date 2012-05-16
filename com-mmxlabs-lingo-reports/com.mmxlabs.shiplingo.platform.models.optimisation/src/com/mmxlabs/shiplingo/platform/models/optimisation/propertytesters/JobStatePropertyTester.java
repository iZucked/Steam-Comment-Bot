package com.mmxlabs.shiplingo.platform.models.optimisation.propertytesters;

import org.eclipse.core.expressions.PropertyTester;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.shiplingo.platform.models.optimisation.Activator;

public class JobStatePropertyTester extends PropertyTester {
	public JobStatePropertyTester() {
	}

	@Override
	public boolean test(final Object receiver, final String property, final Object[] args, final Object expectedValue) {
		final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
		
		if (jobManager != null) {
			if (receiver instanceof ScenarioInstance) {
				final ScenarioInstance instance = (ScenarioInstance) receiver;
				final IJobDescriptor descriptor = jobManager.findJobForResource(instance.getUuid());
				if (descriptor != null) {
					final IJobControl control = jobManager.getControlForJob(descriptor);
					if (control!= null) {
						if (expectedValue != null) {
							return expectedValue.toString().equalsIgnoreCase(control.getJobState().name());
						}
					}
				}
			}
		}
		
		return false;
	}
}
