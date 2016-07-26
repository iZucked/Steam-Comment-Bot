/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.scenarios;

import java.net.URL;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.lingo.its.tests.LiNGOTestDataProvider;
import com.mmxlabs.lingo.its.tests.category.OptimisationTest;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;

@RunWith(value = ShiroRunner.class)
public class TestDemoCases extends AbstractOptimisationResultTester {

	@Test
	@Category(OptimisationTest.class)
	public void testBonnyProblems_Bonny_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/0 Bonny.lingo");

		runScenarioWithGCO(new LiNGOTestDataProvider(url),null);
	}

	@Test
	@Category(OptimisationTest.class)
	public void testBonnyProblems_LateAndLost_DES_Backfill_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/1 late and lost-DES backfill -F- Bonny.lingo");

		runScenarioWithGCO(new LiNGOTestDataProvider(url));
	}

	@Test
	@Category(OptimisationTest.class)
	public void testBonnyProblems_O_LateAndLost_DES_Backfill_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/2 -O- late and lost-DES backfill -F- Bonny.lingo");

		runScenarioWithGCO(new LiNGOTestDataProvider(url));
	}

	@Test
	@Category(OptimisationTest.class)
	public void testDryDockIssues_base_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/0 base.lingo");

		runScenarioWithGCO(new LiNGOTestDataProvider(url));
	}

	@Test
	@Category(OptimisationTest.class)
	public void testDryDockIssues_dryDock55_lateness_removed_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/3 -O- dry dock 55 -F- lateness removed -F- base.lingo");

		runScenarioWithGCO(new LiNGOTestDataProvider(url));
	}

	@Test
	@Category(OptimisationTest.class)
	public void testDryDockIssues_dryDock70_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/4 dry dock 70 -O- -F- -F- base.lingo");

		runScenarioWithGCO(new LiNGOTestDataProvider(url));
	}

	@Test
	@Category(OptimisationTest.class)
	public void testDryDockIssues_charterInGenerated_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/5 charter-in generated - rewire for shorter one -O- -F- -F- base.lingo");

		runScenarioWithGCO(new LiNGOTestDataProvider(url));
	}

	@Test
	@Category(OptimisationTest.class)
	public void testFleetCostOptimisation_fleet_demo_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Fleet cost optimisation/0 fleet demo.lingo");

		runScenarioWithGCO(new LiNGOTestDataProvider(url));
	}

	@Test
	@Category(OptimisationTest.class)
	public void testFleetCostOptimisation_O_fleet_demo_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Fleet cost optimisation/1 -O- fleet demo.lingo");

		runScenarioWithGCO(new LiNGOTestDataProvider(url));
	}

}
