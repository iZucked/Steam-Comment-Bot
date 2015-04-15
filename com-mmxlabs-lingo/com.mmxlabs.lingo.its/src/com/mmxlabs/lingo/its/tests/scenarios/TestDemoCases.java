/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.scenarios;

import java.net.URL;

import org.junit.Test;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;

public class TestDemoCases extends AbstractOptimisationResultTester {

	@Test
	public void testBonnyProblems_Bonny_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/0 Bonny.lingo");

		runScenario(url);
	}

	@Test
	public void testBonnyProblems_LateAndLost_DES_Backfill_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/1 late and lost-DES backfill -F- Bonny.lingo");

		runScenario(url);
	}

	@Test
	public void testBonnyProblems_O_LateAndLost_DES_Backfill_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/2 -O- late and lost-DES backfill -F- Bonny.lingo");

		runScenario(url);
	}

	@Test
	public void testDryDockIssues_base_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/0 base.lingo");

		runScenario(url);
	}

	@Test
	public void testDryDockIssues_dryDock55_lateness_removed_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/3 -O- dry dock 55 -F- lateness removed -F- base.lingo");

		runScenario(url);
	}

	@Test
	public void testDryDockIssues_dryDock70_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/4 dry dock 70 -O- -F- -F- base.lingo");

		runScenario(url);
	}

	@Test
	public void testDryDockIssues_charterInGenerated_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/5 charter-in generated - rewire for shorter one -O- -F- -F- base.lingo");

		runScenario(url);
	}

	@Test
	public void testFleetCostOptimisation_fleet_demo_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Fleet cost optimisation/0 fleet demo.lingo");

		runScenario(url);
	}

	@Test
	public void testFleetCostOptimisation_O_fleet_demo_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Fleet cost optimisation/1 -O- fleet demo.lingo");

		runScenario(url);
	}

}
