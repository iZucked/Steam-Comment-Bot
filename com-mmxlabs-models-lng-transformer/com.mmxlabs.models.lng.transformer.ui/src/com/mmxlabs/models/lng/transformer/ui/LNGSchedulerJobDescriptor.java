/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.io.Serializable;

import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
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

	private OptimisationPlan optimisationPlan;

	private final boolean optimise;

	public LNGSchedulerJobDescriptor(final String name, final ScenarioInstance scenarioInstance, final OptimisationPlan optimisationPlan, final boolean optimise) {
		this.name = name;
		this.scenarioInstance = scenarioInstance;
		this.optimisationPlan = optimisationPlan;
		this.optimise = optimise;
	}

	@Override
	public void dispose() {
		scenarioInstance = null;
		optimisationPlan = null;
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

	public boolean isOptimising() {
		return optimise;
	}

	public OptimisationPlan getOptimisationPlan() {
		return optimisationPlan;
	}
}
