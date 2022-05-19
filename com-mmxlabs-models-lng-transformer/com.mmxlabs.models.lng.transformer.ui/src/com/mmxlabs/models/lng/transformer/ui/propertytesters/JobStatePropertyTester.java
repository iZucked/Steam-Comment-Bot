/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.propertytesters;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.base.Objects;
import com.mmxlabs.models.lng.transformer.ui.jobmanagers.IJobManager;
import com.mmxlabs.models.lng.transformer.ui.jobmanagers.TaskStatus;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class JobStatePropertyTester extends PropertyTester {

	@Override
	public boolean test(final Object receiver, final String property, final Object[] args, final Object expectedValue) {
		return reallyTest(receiver, property, expectedValue);
	}

	private boolean reallyTest(final Object receiver, final String property, final Object expectedValue) {

		final TaskStatus state = getJobState(receiver);

		switch (property) {
		case "jobState": {
			if (expectedValue != null && state != null) {
				return expectedValue.toString().equalsIgnoreCase(state.getStatus());
			}
			return false;
		}
		case "canPlay":
			return state == null || !(state.isSubmitted() || state.isRunning());
		case "canTerminate":
			return state != null && (state.isSubmitted() || state.isRunning());
		case "hasActiveJob":
			return state != null && (state.isSubmitted() || state.isRunning());
		default:
			return false;
		}

	}

	private @Nullable TaskStatus getJobState(final Object receiver) {
		final TaskStatus[] status = new TaskStatus[1];
		if (receiver instanceof final ScenarioInstance instance) {
			final String scenarioUUID = instance.getUuid();
			ServiceHelper.withAllServices(IJobManager.class, null, mgr -> {
				for (final var t : mgr.getTasks()) {
					if (Objects.equal(scenarioUUID, t.job.getScenarioUUID())) {
						// Running job takes priority
						if (t.taskStatus.isSubmitted() || t.taskStatus.isRunning()) {
							status[0] = t.taskStatus;
							return false;
						} else if (status[0] == null) {
							// Record any valid status found if we don't have another one.
							status[0] = t.taskStatus;
						}
					}
				}
				return true;
			});
		}

		return status[0];
	}
}
