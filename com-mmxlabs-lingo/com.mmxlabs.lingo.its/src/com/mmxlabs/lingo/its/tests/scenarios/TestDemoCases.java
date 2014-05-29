/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.scenarios;

import java.net.URL;

import org.junit.Test;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;

public class TestDemoCases extends AbstractOptimisationResultTester {

	@Test
	public void testBonnyProblems_Bonny() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/0 Bonny.lingo");

		runScenario(url);
	}

	@Test
	public void testBonnyProblems_LateAndLost_DES_Backfill() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/1 late and lost-DES backfill -F- Bonny.lingo");

		runScenario(url);
	}

	@Test
	public void testBonnyProblems_O_LateAndLost_DES_Backfill() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/2 -O- late and lost-DES backfill -F- Bonny.lingo");

		runScenario(url);
	}

	@Test
	public void testDryDockIssues_base() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/0 base.lingo");

		runScenario(url);
	}

	@Test
	public void testDryDockIssues_dryDock55_lateness_removed() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/3 -O- dry dock 55 -F- lateness removed -F- base.lingo");

		runScenario(url);
	}

	@Test
	public void testDryDockIssues_dryDock70() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/4 dry dock 70 -O- -F- -F- base.lingo");

		runScenario(url);
	}

	@Test
	public void testDryDockIssues_charterInGenerated() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/5 charter-in generated - rewire for shorter one -O- -F- -F- base.lingo");

		runScenario(url);
	}

	@Test
	public void testFleetCostOptimisation_fleet_demo() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Fleet cost optimisation/0 fleet demo.lingo");

		runScenario(url);
	}

	@Test
	public void testFleetCostOptimisation_O_fleet_demo() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Fleet cost optimisation/1 -O- fleet demo.lingo");

		runScenario(url);
	}

}
