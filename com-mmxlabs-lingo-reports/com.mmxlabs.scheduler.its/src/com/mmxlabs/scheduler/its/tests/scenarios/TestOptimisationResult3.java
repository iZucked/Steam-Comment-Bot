/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.its.tests.scenarios;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import org.junit.Test;

import com.mmxlabs.lngscheduler.emf.extras.IncompleteScenarioException;

public class TestOptimisationResult3 extends AbstractOptimisationResultTester {

	@Test
	public void testFitnessRepeatability() throws IOException, IncompleteScenarioException, InterruptedException {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/TestOptimisationResult3.scenario");

		// ↓ ↓ PASTE PRINTED MAP HERE ↓ ↓ //
		final HashMap<String, Long> originalFitnesses = new HashMap<String, Long>();
		originalFitnesses.put("cargo-scheduler-canal-cost", 10025000L);
		originalFitnesses.put("cargo-scheduler-group-profit", 0L);
		originalFitnesses.put("cargo-scheduler-cost-cooldown", 631537200L);
		originalFitnesses.put("cargo-scheduler-charter-revenue", 0L);
		originalFitnesses.put("cargo-scheduler-lateness", 486000000L);
		originalFitnesses.put("cargo-scheduler-cost-base", 47787321L);
		originalFitnesses.put("cargo-scheduler-cost-lng", 252934626L);
		originalFitnesses.put("cargo-scheduler-charter-cost", 0L);

		final HashMap<String, Long> endFitnesses = new HashMap<String, Long>();
		endFitnesses.put("cargo-scheduler-canal-cost", 2415000L);
		endFitnesses.put("cargo-scheduler-group-profit", 0L);
		endFitnesses.put("cargo-scheduler-cost-cooldown", 613844400L);
		endFitnesses.put("cargo-scheduler-charter-revenue", 0L);
		endFitnesses.put("cargo-scheduler-lateness", 0L);
		endFitnesses.put("cargo-scheduler-cost-base", 26344042L);
		endFitnesses.put("cargo-scheduler-cost-lng", 215192466L);
		endFitnesses.put("cargo-scheduler-charter-cost", 0L);

		// ↑ ↑ PASTE PRINTED MAP HERE ↑ ↑ //

		runScenario(url, originalFitnesses, endFitnesses);

	}
}
