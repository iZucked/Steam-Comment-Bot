/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.ui;

import scenario.Scenario;

import com.mmxlabs.jobmanager.jobs.IJobDescriptor;

/**
 * {@link IJobDescriptor} implementation for a {@link Scenario} optimisation job.
 * 
 * @author Simon Goodall
 * 
 */
public final class LNGSchedulerJobDescriptor implements IJobDescriptor {

	private final String name;

	private Scenario scenario;

	public LNGSchedulerJobDescriptor(final String name, final Scenario scenario) {
		this.name = name;
		this.scenario = scenario;
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
		return scenario;
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
