/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown.chain;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;

public class LNGParameters_ActionPlanSettingsModule extends AbstractModule {

	public static final @NonNull String ACTION_PLAN_TOTAL_EVALUATIONS = "ACTION_PLAN_TOTAL_EVALUATIONS";
	public static final @NonNull String ACTION_PLAN_IN_RUN_EVALUATIONS = "ACTION_PLAN_IN_RUN_EVALUATIONS";
	public static final @NonNull String ACTION_PLAN_MAX_SEARCH_DEPTH = "ACTION_PLAN_MAX_SEARCH_DEPTH";

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
	@Named(ACTION_PLAN_TOTAL_EVALUATIONS)
	private int actionPlanTotalEvals() {
		return settings.getTotalEvaluations();
	}

	@Provides
	@Named(ACTION_PLAN_IN_RUN_EVALUATIONS)
	private int actionPlanInRunEvals() {
		return settings.getInRunEvaluations();
	}

	@Provides
	@Named(ACTION_PLAN_MAX_SEARCH_DEPTH)
	private int actionPlanInRunSearchDepth() {
		return settings.getSearchDepth();
	}

}