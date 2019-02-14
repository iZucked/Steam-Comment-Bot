/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * {@link IJobDescriptor} implementation for a {@link Scenario} optimisation job.
 * 
 * @author Simon Goodall
 * 
 */
public final class LNGRunMultipleSeedsJobDescriptor extends AbstractLNGJobDescriptor {

	public LNGRunMultipleSeedsJobDescriptor(final String name, final ScenarioInstance scenarioInstance, final OptimisationPlan optimisationPlan, final boolean optimise) {
		super(name, scenarioInstance, optimisationPlan, optimise);
	}
}