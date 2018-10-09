/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.sequenceoptimisers.metaheuristic.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

public class LWSTabuOptimiserModule extends AbstractModule {
	public static final String GLOBAL_ITERATIONS = "TABU_GLOBAL_ITERATIONS";
	public static final String NEIGHBOURHOOD_ITERATIONS = "TABU_NEIGHBOURHOOD_ITERATIONS";
	public static final String TABU_LIST_SIZE = "TABU_TABU_LIST_SIZE";
	public static final String SEED = "TABU_SEED";
	
	@Override
	protected void configure() {
	}

	@Provides
	@Named(GLOBAL_ITERATIONS)
	private int getIterations() {
		return 10_000;
	}
	
	@Provides
	@Named(NEIGHBOURHOOD_ITERATIONS)
	private int getLocalIterations() {
		return 1_000;
	}
	
	@Provides
	@Named(TABU_LIST_SIZE)
	private int getTabuSize() {
		return 15;
	}

	@Provides
	@Named(SEED)
	private int getSeed(/*@Named(LocalSearchOptimiserModule.RANDOM_SEED) long seed*/) {
		return (int) 0;
	}	
}
