/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless.utils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.fitness.components.NonOptionalSlotFitnessCoreFactory;

public class LNGHeadlessParameters extends HeadlessParameters {
	public LNGHeadlessParameters() {
		// Search parameters
		setParameter("seed", 0, Integer.class, true);
		setParameter("iterations", 70000, Integer.class, true);

		setParameter("clean-state-iterations", 50000, Integer.class, false);
		setParameter("do-clean-state", false, Boolean.class, false);
		setParameter("lso-jobs", 1, Integer.class, false);
		setParameter("clean-state-jobs", 1, Integer.class, false);

//		ADD STUFF HERE
		// LSO
		setParameter("sa-epoch-length", 10000, Integer.class, true);
		setParameter("sa-temperature", 45000, Integer.class, true);
		setParameter("sa-cooling", 0.85, Double.class, true);

		// Scenario parameters
		setParameter("scenario", "", String.class, true);
		setParameter("scenario-path", "", String.class, true);
		setParameter("generatedcharterouts-optimisation", false, Boolean.class, true);
		setParameter("shippingonly-optimisation", false, Boolean.class, true);
		setParameter("periodOptimisationDateBefore", null, YearMonth.class, false);
		setParameter("periodOptimisationDateAfter", null, YearMonth.class, false);
		setParameter("promptStart", null, LocalDate.class, false);
		setParameter("promptEnd", null, LocalDate.class, false);

		// Objectives
		Map<String, Double> defaultObjectives = new HashMap<String, Double>();
		defaultObjectives.put("cargo-scheduler-group-profit", 1.0);
		defaultObjectives.put(CargoSchedulerFitnessCoreFactory.LATENESS_COMPONENT_NAME, 1000000.0);
		defaultObjectives.put(CargoSchedulerFitnessCoreFactory.CAPACITY_COMPONENT_NAME, 5000000.0);
		defaultObjectives.put(NonOptionalSlotFitnessCoreFactory.NAME, 3000000.0);

		setParameter("objectives", new DoubleMap(defaultObjectives), DoubleMap.class);

		// Lateness special
		setParameter("lcp-set-prompt-period", 48, Integer.class, false);
		setParameter("lcp-set-prompt-lowWeight", 1, Integer.class, false);
		setParameter("lcp-set-prompt-highWeight", 1, Integer.class, false);

		setParameter("lcp-set-midTerm-period", 72, Integer.class, false);
		setParameter("lcp-set-midTerm-lowWeight", 1, Integer.class, false);
		setParameter("lcp-set-midTerm-highWeight", 1, Integer.class, false);

		setParameter("lcp-set-beyond-period", 72, Integer.class, false);
		setParameter("lcp-set-beyond-lowWeight", 1, Integer.class, false);
		setParameter("lcp-set-beyond-highWeight", 1, Integer.class, false);

		// Similarity
		setParameter("scp-set-low-thresh", 8, Integer.class, false);
		setParameter("scp-set-low-weight", 0, Integer.class, false);
		
		setParameter("scp-set-med-thresh", 16, Integer.class, false);
		setParameter("scp-set-med-weight", 0, Integer.class, false);
		
		setParameter("scp-set-high-thresh", 30, Integer.class, false);
		setParameter("scp-set-high-weight", 0, Integer.class, false);
		
		setParameter("scp-set-outOfBounds-thresh", -1, Integer.class, false);
		setParameter("scp-set-outOfBounds-weight", 0, Integer.class, false);
		
		// Restarting
		setParameter("restarting-useRestarting", false, Boolean.class, false);
		setParameter("restarting-restartThreshold", 0, Integer.class, false);

		// Hill Climbing
		setParameter("hillClimbing-useHillClimbing", false, Boolean.class, false);
		setParameter("hillClimbing-iterations", 0, Integer.class, false);
		
		// Action Sets
		setParameter("actionSets-buildActionSets", false, Boolean.class, false);
		setParameter("actionSets-totalEvals", 5_000_000, Integer.class, false);
		setParameter("actionSets-inRunEvals", 1_500_000, Integer.class, false);
		setParameter("actionSets-maxSearchDepth", 5_000, Integer.class, false);
		setParameter("actionSets-verboseLogging", false, Boolean.class, false);
		// Parallel
		setParameter("actionSets-maxThreads", 1, Integer.class, false);
		
		// Moves
		setParameter("moves-useLoopingSCMG", false, Boolean.class, false);
		
		//Idle Time
		setParameter("idle-time-low",2_500, Integer.class, false);
		setParameter("idle-time-high",10_000, Integer.class, false);
		setParameter("idle-time-end", 10_000, Integer.class, false);
		
		setParameter("spotmarket-optimisation", false, Boolean.class, false);
		
		setParameter("use-roulette-wheel", false, Boolean.class, false);
		
		//Move Distributions
		setParameter("equal-move-distributions", false, Boolean.class, false);
		setParameter("insert-optional-frequency", 0.1, Double.class, false);
		setParameter("remove-optional-frequency", 0.2, Double.class, false);
		setParameter("swap-segments-frequency", 0.4, Double.class, false);
		setParameter("move-segments-frequency", 0.6, Double.class, false);
		setParameter("swap-tails-frequency", 0.8, Double.class, false);
		setParameter("shuffle-elements-frequency", 1.0, Double.class, false);
		
		setParameter("use-guided-moves", false, Boolean.class, false);
		setParameter("use-legacy-check", false, Boolean.class, false);
	}
}
