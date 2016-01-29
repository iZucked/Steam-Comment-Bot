/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import com.google.inject.AbstractModule;

public class ActionPlanModule extends AbstractModule{

	public static final String ACTION_PLAN_TOTAL_EVALUATIONS = "ACTION_PLAN_TOTAL_EVALUATIONS";
	public static final String ACTION_PLAN_IN_RUN_EVALUATIONS = "ACTION_PLAN_IN_RUN_EVALUATIONS";
	public static final String ACTION_PLAN_MAX_SEARCH_DEPTH = "ACTION_PLAN_MAX_SEARCH_DEPTH";
	@Override
	protected void configure() {
	}
}
