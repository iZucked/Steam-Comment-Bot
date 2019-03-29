/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.csv.scenarios;

import java.net.URL;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.lingo.its.tests.CSVTestDataProvider;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;

@ExtendWith(ShiroRunner.class)
public class TestCSVDemoCases extends AbstractOptimisationResultTester {

	@Test
	@Tag(TestCategories.OPTIMISATION_TEST)
	public void testBonnyProblems_Bonny() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/0 Bonny.scenario - CSV/");

		runScenarioWithGCO(new CSVTestDataProvider(url));
	}

	@Test
	@Tag(TestCategories.OPTIMISATION_TEST)
	public void testBonnyProblems_LateAndLost_DES_Backfill() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/1 late and lost-DES backfill -F- Bonny.scenario - CSV/");

		runScenarioWithGCO(new CSVTestDataProvider(url));
	}

	@Test
	@Tag(TestCategories.OPTIMISATION_TEST)
	public void testBonnyProblems_O_LateAndLost_DES_Backfill() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/2 -O- late and lost-DES backfill -F- Bonny.scenario - CSV/");

		runScenarioWithGCO(new CSVTestDataProvider(url));
	}

	@Test
	@Tag(TestCategories.OPTIMISATION_TEST)
	public void testDryDockIssues_base() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/0 base.scenario - CSV/");
		runScenarioWithGCO(new CSVTestDataProvider(url));
	}

	@Test
	@Tag(TestCategories.OPTIMISATION_TEST)
	public void testDryDockIssues_dryDock55_lateness_removed() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/3 -O- dry dock 55 -F- lateness removed -F- base.scenario - CSV/");
		runScenarioWithGCO(new CSVTestDataProvider(url));

	}

	@Test
	@Tag(TestCategories.OPTIMISATION_TEST)
	public void testDryDockIssues_dryDock70() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/4 dry dock 70 -O- -F- -F- base.scenario - CSV/");

		runScenarioWithGCO(new CSVTestDataProvider(url));
	}

	@Test
	@Tag(TestCategories.OPTIMISATION_TEST)
	public void testDryDockIssues_charterInGenerated() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/5 charter-in generated - rewire for shorter one -O- -F- -F- base.scenario - CSV/");

		runScenarioWithGCO(new CSVTestDataProvider(url));
	}

	@Test
	@Tag(TestCategories.OPTIMISATION_TEST)
	public void testFleetCostOptimisation_fleet_demo() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Fleet cost optimisation/0 fleet demo-scenario.scenario - CSV/");
		runScenarioWithGCO(new CSVTestDataProvider(url));
	}

	@Test
	@Tag(TestCategories.OPTIMISATION_TEST)
	public void testFleetCostOptimisation_O_fleet_demo() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Fleet cost optimisation/1 -O- fleet demo-scenario.scenario - CSV/");

		runScenarioWithGCO(new CSVTestDataProvider(url));
	}

}
