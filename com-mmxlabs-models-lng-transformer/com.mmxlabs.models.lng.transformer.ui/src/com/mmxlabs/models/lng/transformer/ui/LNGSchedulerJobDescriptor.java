/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.io.Serializable;

import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * {@link IJobDescriptor} implementation for a {@link Scenario} optimisation job.
 * 
 * @author Simon Goodall
 * 
 */
public final class LNGSchedulerJobDescriptor implements IJobDescriptor, Serializable {
	private static final long serialVersionUID = 1L;

	private final String name;

	private ScenarioInstance scenarioInstance;

	private boolean optimise;
	
	private final String lockKey;

	public LNGSchedulerJobDescriptor(final String name, final ScenarioInstance scenarioInstance, final boolean optimise, final String lockKey) {
		this.name = name;
		this.scenarioInstance = scenarioInstance;
		this.optimise = optimise;
		this.lockKey = lockKey;
	}

	@Override
	public void dispose() {
		scenarioInstance = null;
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

	public String getLockKey() {
		return lockKey;
	}

	public boolean isOptimising() {
		return optimise;
	}
}
