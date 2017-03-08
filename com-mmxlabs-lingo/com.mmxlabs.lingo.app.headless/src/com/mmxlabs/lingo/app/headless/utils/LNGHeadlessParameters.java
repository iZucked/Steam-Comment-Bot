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

		// Scenario parameters
		setParameter("scenario", "", String.class, true);
		setParameter("scenario-path", "", String.class, true);

		// Search Parameters
		setParameter("seed", 0, Integer.class, true);
		setParameter("iterations", 70000, Integer.class, true);

		// Period Data
		setParameter("period", true, Boolean.class, true);
		Map<String, Object> defaultPeriod = new HashMap<>();
		defaultPeriod.put("period-optimisation-date-before", YearMonth.of(1970, 1)); // YearMonth
		defaultPeriod.put("period-optimisation-date-after", YearMonth.of(1970, 1));
		defaultPeriod.put("prompt-start", LocalDate.of(1970, 1, 1)); // LocalDate
		defaultPeriod.put("prompt-end", LocalDate.of(1970, 1, 1));
		setParameter("period-data", new JMap(defaultPeriod), JMap.class, true);

		// LSO
		setParameter("lso-jobs", 1, Integer.class, false);
		Map<String, Object> defaultLSO = new HashMap<>();
		defaultLSO.put("epoch-length", 10_000);
		defaultLSO.put("initial-temperature", 45_000);
		defaultLSO.put("cooling-factor", 0.85);
		setParameter("simulated-annealing", new JMap(defaultLSO), JMap.class, true);

		// Restarting
		Map<String, Object> defaultRestarting = new HashMap<>();
		defaultRestarting.put("active", false);
		defaultRestarting.put("threshold", 0);
		setParameter("restarting", new JMap(defaultRestarting), JMap.class, true);

		// Hill-Climbing
		Map<String, Object> defaultHillClimbing = new HashMap<>();
		defaultHillClimbing.put("active", false);
		defaultHillClimbing.put("iterations", 50_000);
		setParameter("hill-climbing", new JMap(defaultHillClimbing), JMap.class, true);

		// Action Sets
		setParameter("action-sets", false, Boolean.class, true);
		Map<String, Object> actionSettingsDefault = new HashMap<>();
		actionSettingsDefault.put("verbose-logging", false);
		actionSettingsDefault.put("in-run-evals", 2_000_000);
		actionSettingsDefault.put("total-evals", 30_000_000);
		actionSettingsDefault.put("max-search-depth", 5_000);
		setParameter("action-sets-data", new JMap(actionSettingsDefault), JMap.class, true);

		// Lateness
		Map<String, Object> defaultLateness = new HashMap<>();
		defaultLateness.put("prompt-period", 48);
		defaultLateness.put("prompt-low", 1);
		defaultLateness.put("prompt-high", 1);

		defaultLateness.put("mid-term-period", 72);
		defaultLateness.put("mid-term-low", 1);
		defaultLateness.put("mid-term-high", 1);

		defaultLateness.put("beyond-period", 72);
		defaultLateness.put("beyond-low", 1);
		defaultLateness.put("beyond-high", 1);
		setParameter("lateness-weights", new JMap(defaultLateness), JMap.class);

		// Similarity
		Map<String, Object> defaultSimilarity = new HashMap<>();
		defaultSimilarity.put("high-thresh", 30);
		defaultSimilarity.put("high-weight", 0);
		defaultSimilarity.put("low-thresh", 8);
		defaultSimilarity.put("low-weight", 0);
		defaultSimilarity.put("med-thresh", 16);
		defaultSimilarity.put("med-weight", 0);
		defaultSimilarity.put("out-of-bounds-weight", 0);
		setParameter("similarity", new JMap(defaultSimilarity), JMap.class);

		// Optimisation specifics
		setParameter("shipping-only-optimisation", false, Boolean.class, true);
		setParameter("charter-outs-optimisation", true, Boolean.class, true);
		setParameter("spot-market-optimisation", false, Boolean.class, true);

		// Objectives
		Map<String, Object> defaultObjectives = new HashMap<>();
		defaultObjectives.put("cargo-scheduler-group-profit", 1.0);
		defaultObjectives.put(CargoSchedulerFitnessCoreFactory.LATENESS_COMPONENT_NAME, 1000000.0);
		defaultObjectives.put(CargoSchedulerFitnessCoreFactory.CAPACITY_COMPONENT_NAME, 5000000.0);
		defaultObjectives.put(NonOptionalSlotFitnessCoreFactory.NAME, 3000000.0);
		setParameter("objectives", new JMap(defaultObjectives), JMap.class, true);

		// Moves
		setParameter("use-roulette-wheel", false, Boolean.class, false);
		setParameter("equal-move-distributions", true, Boolean.class, false);
		Map<String, Object> defaultMoveDistributions = new HashMap<>();
		defaultMoveDistributions.put("insert-optional-frequency", 0.1);
		defaultMoveDistributions.put("move-segments-frequency", 0.3);
		defaultMoveDistributions.put("remove-optional-frequency", 0.5);
		defaultMoveDistributions.put("shuffle-elements-frequency", 0.6);
		defaultMoveDistributions.put("swap-segments-frequency", 0.8);
		defaultMoveDistributions.put("swap-tails-frequency", 1.0);
		setParameter("move-distributions", new JMap(defaultMoveDistributions), JMap.class, false);

		// Parallel
		setParameter("pool-size", 1, Integer.class, false);

		// Idle Time
		Map<String, Object> defaultIdleWeights = new HashMap<>();
		defaultIdleWeights.put("idle-time-low", 2_500);
		defaultIdleWeights.put("idle-time-high", 10_000);
		defaultIdleWeights.put("idle-time-end", 10_000);
		setParameter("idle-weights", new JMap(defaultIdleWeights), JMap.class, false);

	}
}
