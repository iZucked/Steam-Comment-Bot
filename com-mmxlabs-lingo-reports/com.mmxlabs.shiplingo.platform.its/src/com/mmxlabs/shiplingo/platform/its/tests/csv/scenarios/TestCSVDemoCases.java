/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.its.tests.csv.scenarios;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;

import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.shiplingo.platform.its.tests.scenarios.AbstractOptimisationResultTester;

public class TestCSVDemoCases extends AbstractOptimisationResultTester {

	@Test
	public void testBonnyProblems_Bonny() throws IOException, InterruptedException, IncompleteScenarioException {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/0 Bonny.scenario - CSV/");

		MMXRootObject scenario = CSVImporter.importCSVScenario(url.toString());
		runScenario(scenario, new URL(url.toString() + "fitness"));

	}

	@Test
	public void testBonnyProblems_LateAndLost_DES_Backfill() throws IOException, InterruptedException, IncompleteScenarioException {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/1 late and lost-DES backfill -F- Bonny.scenario - CSV/");

		MMXRootObject scenario = CSVImporter.importCSVScenario(url.toString());
		runScenario(scenario, new URL(url.toString() + "fitness"));

	}

	@Test
	public void testBonnyProblems_O_LateAndLost_DES_Backfill() throws IOException, InterruptedException, IncompleteScenarioException {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/2 -O- late and lost-DES backfill -F- Bonny.scenario - CSV/");

		MMXRootObject scenario = CSVImporter.importCSVScenario(url.toString());
		runScenario(scenario, new URL(url.toString() + "fitness"));

	}

	@Test
	public void testDryDockIssues_base() throws IOException, InterruptedException, IncompleteScenarioException {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/0 base.scenario - CSV/");

		MMXRootObject scenario = CSVImporter.importCSVScenario(url.toString());
		runScenario(scenario, new URL(url.toString() + "fitness"));

	}

	@Test
	public void testDryDockIssues_dryDock55_lateness_removed() throws IOException, InterruptedException, IncompleteScenarioException {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/3 -O- dry dock 55 -F- lateness removed -F- base.scenario - CSV/");

		MMXRootObject scenario = CSVImporter.importCSVScenario(url.toString());
		runScenario(scenario, new URL(url.toString() + "fitness"));

	}

	@Test
	public void testDryDockIssues_dryDock70() throws IOException, InterruptedException, IncompleteScenarioException {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/4 dry dock 70 -O- -F- -F- base.scenario - CSV/");

		MMXRootObject scenario = CSVImporter.importCSVScenario(url.toString());
		runScenario(scenario, new URL(url.toString() + "fitness"));

	}

	@Test
	public void testDryDockIssues_charterInGenerated() throws IOException, InterruptedException, IncompleteScenarioException {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/5 charter-in generated - rewire for shorter one -O- -F- -F- base.scenario - CSV/");

		MMXRootObject scenario = CSVImporter.importCSVScenario(url.toString());
		runScenario(scenario, new URL(url.toString() + "fitness"));

	}

	@Test
	public void testFleetCostOptimisation_fleet_demo() throws IOException, InterruptedException, IncompleteScenarioException {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Fleet cost optimisation/0 fleet demo-scenario.scenario - CSV/");

		MMXRootObject scenario = CSVImporter.importCSVScenario(url.toString());
		runScenario(scenario, new URL(url.toString() + "fitness"));

	}

	@Test
	public void testFleetCostOptimisation_O_fleet_demo() throws IOException, InterruptedException, IncompleteScenarioException {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Fleet cost optimisation/1 -O- fleet demo-scenario.scenario - CSV/");

		MMXRootObject scenario = CSVImporter.importCSVScenario(url.toString());
		runScenario(scenario, new URL(url.toString() + "fitness"));

	}

}
