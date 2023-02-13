/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.sequenceoptimisers.metaheuristic.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.parameters.CleanStateOptimisationSettings;

public class LWSTabuOptimiserModule extends AbstractModule {
	public static final String GLOBAL_ITERATIONS = "TABU_GLOBAL_ITERATIONS";
	public static final String NEIGHBOURHOOD_ITERATIONS = "TABU_NEIGHBOURHOOD_ITERATIONS";
	public static final String TABU_LIST_SIZE = "TABU_TABU_LIST_SIZE";
	public static final String SEED = "TABU_SEED";
	private CleanStateOptimisationSettings cleanStateOptimisationSettings;
	
	public LWSTabuOptimiserModule(CleanStateOptimisationSettings cleanStateOptimisationSettings) {
		this.cleanStateOptimisationSettings = cleanStateOptimisationSettings;
	}
	
	@Override
	protected void configure() {
	}

	@Provides
	@Named(GLOBAL_ITERATIONS)
	private int getIterations() {
		return cleanStateOptimisationSettings.getGlobalIterations();
	}
	
	@Provides
	@Named(NEIGHBOURHOOD_ITERATIONS)
	private int getLocalIterations() {
		return cleanStateOptimisationSettings.getLocalIterations();
	}
	
	@Provides
	@Named(TABU_LIST_SIZE)
	private int getTabuSize() {
		return cleanStateOptimisationSettings.getTabuSize();
	}

	@Provides
	@Named(SEED)
	private int getSeed(/*@Named(LocalSearchOptimiserModule.RANDOM_SEED) long seed*/) {
		return cleanStateOptimisationSettings.getSeed();
	}	
}
