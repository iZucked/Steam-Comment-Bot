/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.optimisation;

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

	public LNGSchedulerJobDescriptor(final String name, final ScenarioInstance scenarioInstance, final boolean optimise) {
		this.name = name;
		this.scenarioInstance = scenarioInstance;
		this.optimise = optimise;
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

	/**
	 * @return
	 */
	public boolean isOptimising() {
		return optimise;
	}

	public void setOptimising(final boolean optimise) {
		this.optimise = optimise;
	}
}
