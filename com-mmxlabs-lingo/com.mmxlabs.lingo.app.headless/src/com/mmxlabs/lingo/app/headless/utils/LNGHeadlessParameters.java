package com.mmxlabs.lingo.app.headless.utils;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.LocalDate;
import org.joda.time.YearMonth;

import com.mmxlabs.optimiser.common.fitness.NonOptionalSlotFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCoreFactory;

public class LNGHeadlessParameters extends HeadlessParameters {
	public LNGHeadlessParameters() {
		// Search parameters
		setParameter("seed", 0, Integer.class, true);
		setParameter("iterations", 70000, Integer.class, true);

		// LSO
		setParameter("sa-epoch-length", 10000, Integer.class, true);
		setParameter("sa-temperature", 45000, Integer.class, true);
		setParameter("sa-cooling", 0.85, Double.class, true);

		// Scenario parameters
		setParameter("scenario", "", String.class, true);
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
		setParameter("scp-set-low-weight", 1000000, Integer.class, false);
		
		setParameter("scp-set-med-thresh", 16, Integer.class, false);
		setParameter("scp-set-med-weight", 1000000, Integer.class, false);
		
		setParameter("scp-set-high-thresh", 30, Integer.class, false);
		setParameter("scp-set-high-weight", 1000000, Integer.class, false);
		
		setParameter("scp-set-outOfBounds-thresh", -1, Integer.class, false);
		setParameter("scp-set-outOfBounds-weight", 1000000, Integer.class, false);
		
		// Similarity settings
		setParameter("similarity-thresholdNumChanges", -1, Integer.class, false);

	}
}
