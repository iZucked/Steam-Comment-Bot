/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.actionablesets;

import java.io.Serializable;
import java.util.List;

import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * {@link IJobDescriptor} implementation for a {@link Scenario} optimisation job.
 * 
 * @author Simon Goodall
 * 
 */
public final class CreateActionableSetPlanJobDescriptor implements IJobDescriptor, Serializable {
	private static final long serialVersionUID = 1L;

	private final String name;

	private ScenarioInstance scenarioInstance;

	private UserSettings userSettings;

	private List<Slot> targetSlots;

	public UserSettings getUserSettings() {
		return userSettings;
	}

	public CreateActionableSetPlanJobDescriptor(final String name, final ScenarioInstance scenarioInstance, final UserSettings userSettings) {
		this.name = name;
		this.scenarioInstance = scenarioInstance;
		this.userSettings = userSettings;
		this.targetSlots = targetSlots;
	}

	@Override
	public void dispose() {
		scenarioInstance = null;
		userSettings = null;
		targetSlots = null;
	}

	@Override
	public String getJobName() {
		return name;
	}

	@Override
	public ScenarioInstance getJobContext() {
		return scenarioInstance;
	}

	@Override
	public Object getJobMetadata() {
		return null;
	}

	@Override
	public Object getJobType() {
		return null;
	}
}
