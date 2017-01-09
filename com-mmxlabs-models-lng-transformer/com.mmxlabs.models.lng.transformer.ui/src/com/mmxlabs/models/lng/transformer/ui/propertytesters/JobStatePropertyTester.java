/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.propertytesters;

import org.eclipse.core.expressions.PropertyTester;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class JobStatePropertyTester extends PropertyTester {
	public JobStatePropertyTester() {
	}

	@Override
	public boolean test(final Object receiver, final String property, final Object[] args, final Object expectedValue) {
		boolean b = reallyTest(receiver, property, expectedValue);
		// if (receiver instanceof ScenarioInstance) System.err.println(((ScenarioInstance) receiver).getName() + "." + property + "=" + b);
		return b;
	}

	private boolean reallyTest(final Object receiver, final String property, final Object expectedValue) {

		final EJobState state = getJobState(receiver);

		if ("jobState".equals(property)) {
			if (expectedValue != null && state != null) {
				return expectedValue.toString().equalsIgnoreCase(state.name());
			}
		} else if ("canPause".equals(property)) {
			if (state == EJobState.RUNNING)
				return true;
		} else if ("canPlay".equals(property)) {
			if (state == null) {
				return true;
			}
			switch (state) {
			case CANCELLED:
			case COMPLETED:
			case PAUSED:
			case INITIALISED:
			case CREATED:
				return true;
			default:
				return false;
			}
		} else if ("canTerminate".equals(property)) {
			if (state == null) {
				return false;
			}
			switch (state) {
			case RUNNING:
			case PAUSED:
				return true;
			default:
				return false;
			}
		} else if ("hasActiveJob".equals(property)) {
			if (state == null)
				return false;
			switch (state) {
			case RUNNING:
			case PAUSED:
			case PAUSING:
			case INITIALISED:
			case INITIALISING:
			case CANCELLING:
			case RESUMING:
				return true;
			default:
				return false;
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
					if (control != null) {
						return control.getJobState();
					}
				}
			}
		}
		return null;
	}
}
