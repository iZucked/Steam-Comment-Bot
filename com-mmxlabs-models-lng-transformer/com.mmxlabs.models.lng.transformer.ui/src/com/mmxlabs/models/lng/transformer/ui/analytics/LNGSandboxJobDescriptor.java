/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.io.Serializable;
import java.util.function.Function;

import org.eclipse.core.runtime.IProgressMonitor;

import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * {@link IJobDescriptor} implementation for a {@link Scenario} optimisation job.
 * 
 * @author Simon Goodall
 * 
 */
public final class LNGSandboxJobDescriptor implements IJobDescriptor, Serializable {
	private static final long serialVersionUID = 1L;

	private final String name;

	private ScenarioInstance scenarioInstance;

	private Function<IProgressMonitor, AbstractSolutionSet> action;

	public LNGSandboxJobDescriptor(final String name, final ScenarioInstance scenarioInstance, Function<IProgressMonitor, AbstractSolutionSet> action) {
		this.name = name;
		this.scenarioInstance = scenarioInstance;
		this.action = action;
	}

	@Override
	public void dispose() {
		scenarioInstance = null;
		this.action = null;

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

	public AbstractSolutionSet run(IProgressMonitor progressMonitor) {
		return action.apply(progressMonitor);
	}
}
