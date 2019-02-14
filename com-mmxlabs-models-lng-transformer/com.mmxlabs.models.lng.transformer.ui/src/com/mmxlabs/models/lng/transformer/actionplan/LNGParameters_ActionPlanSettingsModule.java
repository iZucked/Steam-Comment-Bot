/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.actionplan;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.scheduler.optimiser.actionplan.BagOptimiser;

public class LNGParameters_ActionPlanSettingsModule extends AbstractModule {

	@NonNull
	private final ActionPlanOptimisationStage settings;

	public LNGParameters_ActionPlanSettingsModule(@NonNull final ActionPlanOptimisationStage settings) {
		this.settings = settings;
	}

	@Override
	protected void configure() {
		// Nothing to do here
	}

	@Provides
	@Named(BagOptimiser.ACTION_PLAN_TOTAL_EVALUATIONS)
	private int actionPlanTotalEvals() {
		return settings.getTotalEvaluations();
	}

	@Provides
	@Named(BagOptimiser.ACTION_PLAN_IN_RUN_EVALUATIONS)
	private int actionPlanInRunEvals() {
		return settings.getInRunEvaluations();
	}

	@Provides
	@Named(BagOptimiser.ACTION_PLAN_MAX_SEARCH_DEPTH)
	private int actionPlanInRunSearchDepth() {
		return settings.getSearchDepth();
	}

}