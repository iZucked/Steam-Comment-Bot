/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.optimisation;

import java.io.Serializable;

import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.mmxcore.MMXRootObject;

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

	public LNGSchedulerJobDescriptor(final String name, final MMXRootObject scenario) {
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
	public MMXRootObject getJobContext() {
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
