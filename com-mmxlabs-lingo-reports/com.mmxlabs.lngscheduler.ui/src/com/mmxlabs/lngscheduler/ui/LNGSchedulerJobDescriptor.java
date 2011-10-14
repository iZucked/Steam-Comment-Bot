/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.ui;

import java.io.Serializable;

import scenario.Scenario;

import com.mmxlabs.jobmanager.jobs.IJobDescriptor;

/**
 * {@link IJobDescriptor} implementation for a {@link Scenario} optimisation job.
 * 
 * @author Simon Goodall
 * 
 */
public final class LNGSchedulerJobDescriptor implements IJobDescriptor, Serializable {
	private static final long serialVersionUID = 1L;

	private final String name;

	private SerializableScenario scenario;

	// private Scenario scenario;

	public LNGSchedulerJobDescriptor(final String name, final Scenario scenario) {
		this.name = name;
		this.scenario = new SerializableScenario(scenario);
		// this.scenario = scenario;
	}

	@Override
	public void dispose() {
		scenario = null;
	}

	@Override
	public String getJobName() {
		return name;
	}

	@Override
	public Scenario getJobContext() {
		return scenario.scenario;
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
