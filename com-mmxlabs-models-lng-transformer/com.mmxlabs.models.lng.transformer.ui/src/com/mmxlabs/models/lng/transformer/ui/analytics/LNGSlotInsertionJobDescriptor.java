/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.io.Serializable;
import java.util.List;

import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * {@link IJobDescriptor} implementation for a {@link Scenario} optimisation job.
 * 
 * @author Simon Goodall
 * 
 */
public final class LNGSlotInsertionJobDescriptor implements IJobDescriptor, Serializable {
	private static final long serialVersionUID = 1L;

	private final String name;

	private ScenarioInstance scenarioInstance;

	private UserSettings userSettings;

	private List<Slot> targetSlots;
	private List<VesselEvent> targetEvents;

	public UserSettings getUserSettings() {
		return userSettings;
	}

	public List<Slot> getTargetSlots() {
		return targetSlots;
	}

	public List<VesselEvent> getTargetEvents() {
		return targetEvents;
	}

	public LNGSlotInsertionJobDescriptor(final String name, final ScenarioInstance scenarioInstance, final UserSettings userSettings, List<Slot> targetSlots, List<VesselEvent> targetEvents) {
		this.name = name;
		this.scenarioInstance = scenarioInstance;
		this.userSettings = userSettings;
		this.targetSlots = targetSlots;
		this.targetEvents = targetEvents;
	}

	@Override
	public void dispose() {
		scenarioInstance = null;
		userSettings = null;
		targetSlots = null;
		targetEvents = null;
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
