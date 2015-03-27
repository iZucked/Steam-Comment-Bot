/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.scenarios;

import java.net.URL;

import org.junit.Test;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.lingo.its.tests.ReportTester;

public class TestDemoCases extends AbstractOptimisationResultTester {

	@Test
	public void testBonnyProblems_Bonny_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/0 Bonny.lingo");

		runScenario(url);
	}

	@Test
	public void testBonnyProblems_Bonny_VerticalReport() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/0 Bonny.lingo");
		testReports(url, ReportTester.VERTICAL_REPORT_ID, "html");
	}

	@Test
	public void testBonnyProblems_Bonny_ScheduleSummary() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/0 Bonny.lingo");
		testReports(url, ReportTester.SCHEDULE_SUMMARY_ID, "html");
	}

	@Test
	public void testBonnyProblems_Bonny_PortRotations() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/0 Bonny.lingo");
		testReports(url, ReportTester.PORT_ROTATIONS_ID, "html");
	}

	@Test
	public void testBonnyProblems_LateAndLost_DES_Backfill_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/1 late and lost-DES backfill -F- Bonny.lingo");

		runScenario(url);
	}

	@Test
	public void testBonnyProblems_LateAndLost_DES_Backfill_VerticalReport() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/1 late and lost-DES backfill -F- Bonny.lingo");
		testReports(url, ReportTester.VERTICAL_REPORT_ID, "html");
	}

	@Test
	public void testBonnyProblems_LateAndLost_DES_Backfill_ScheduleSummary() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/1 late and lost-DES backfill -F- Bonny.lingo");
		testReports(url, ReportTester.SCHEDULE_SUMMARY_ID, "html");
	}

	@Test
	public void testBonnyProblems_LateAndLost_DES_Backfill_PortRotations() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/1 late and lost-DES backfill -F- Bonny.lingo");
		testReports(url, ReportTester.PORT_ROTATIONS_ID, "html");
	}

	@Test
	public void testBonnyProblems_O_LateAndLost_DES_Backfill_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/2 -O- late and lost-DES backfill -F- Bonny.lingo");

		runScenario(url);
	}

	@Test
	public void testBonnyProblems_O_LateAndLost_DES_Backfill_VerticalReport() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/2 -O- late and lost-DES backfill -F- Bonny.lingo");
		testReports(url, ReportTester.VERTICAL_REPORT_ID, "html");
	}

	@Test
	public void testBonnyProblems_O_LateAndLost_DES_Backfill_ScheduleSummary() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/2 -O- late and lost-DES backfill -F- Bonny.lingo");
		testReports(url, ReportTester.SCHEDULE_SUMMARY_ID, "html");
	}

	@Test
	public void testBonnyProblems_O_LateAndLost_DES_Backfill_PortRotations() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Bonny problems/2 -O- late and lost-DES backfill -F- Bonny.lingo");
		testReports(url, ReportTester.PORT_ROTATIONS_ID, "html");
	}

	@Test
	public void testDryDockIssues_base_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/0 base.lingo");

		runScenario(url);
	}

	@Test
	public void testDryDockIssues_base_VerticalReport() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/0 base.lingo");
		testReports(url, ReportTester.VERTICAL_REPORT_ID, "html");
	}

	@Test
	public void testDryDockIssues_base_ScheduleSummary() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/0 base.lingo");
		testReports(url, ReportTester.SCHEDULE_SUMMARY_ID, "html");
	}

	@Test
	public void testDryDockIssues_base_PortRotations() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/0 base.lingo");
		testReports(url, ReportTester.PORT_ROTATIONS_ID, "html");
	}

	@Test
	public void testDryDockIssues_dryDock55_lateness_removed_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/3 -O- dry dock 55 -F- lateness removed -F- base.lingo");

		runScenario(url);
	}

	@Test
	public void testDryDockIssues_dryDock55_lateness_removed_VerticalReport() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/3 -O- dry dock 55 -F- lateness removed -F- base.lingo");
		testReports(url, ReportTester.VERTICAL_REPORT_ID, "html");
	}

	@Test
	public void testDryDockIssues_dryDock55_lateness_removed_ScheduleSummary() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/3 -O- dry dock 55 -F- lateness removed -F- base.lingo");
		testReports(url, ReportTester.SCHEDULE_SUMMARY_ID, "html");
	}

	@Test
	public void testDryDockIssues_dryDock55_lateness_removed_PortRotations() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/3 -O- dry dock 55 -F- lateness removed -F- base.lingo");
		testReports(url, ReportTester.PORT_ROTATIONS_ID, "html");
	}

	@Test
	public void testDryDockIssues_dryDock70_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/4 dry dock 70 -O- -F- -F- base.lingo");

		runScenario(url);
	}

	@Test
	public void testDryDockIssues_dryDock70_VerticalReport() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/4 dry dock 70 -O- -F- -F- base.lingo");
		testReports(url, ReportTester.VERTICAL_REPORT_ID, "html");
	}

	@Test
	public void testDryDockIssues_dryDock70_ScheduleSummary() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/4 dry dock 70 -O- -F- -F- base.lingo");
		testReports(url, ReportTester.SCHEDULE_SUMMARY_ID, "html");
	}

	@Test
	public void testDryDockIssues_dryDock70_PortRotations() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/4 dry dock 70 -O- -F- -F- base.lingo");
		testReports(url, ReportTester.PORT_ROTATIONS_ID, "html");
	}

	@Test
	public void testDryDockIssues_charterInGenerated_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/5 charter-in generated - rewire for shorter one -O- -F- -F- base.lingo");

		runScenario(url);
	}

	@Test
	public void testDryDockIssues_charterInGenerated_VerticalReport() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/5 charter-in generated - rewire for shorter one -O- -F- -F- base.lingo");
		testReports(url, ReportTester.VERTICAL_REPORT_ID, "html");
	}

	@Test
	public void testDryDockIssues_charterInGenerated_ScheduleSummary() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/5 charter-in generated - rewire for shorter one -O- -F- -F- base.lingo");
		testReports(url, ReportTester.SCHEDULE_SUMMARY_ID, "html");
	}

	@Test
	public void testDryDockIssues_charterInGenerated_PortRotations() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Dry dock issues/5 charter-in generated - rewire for shorter one -O- -F- -F- base.lingo");
		testReports(url, ReportTester.PORT_ROTATIONS_ID, "html");
	}

	@Test
	public void testFleetCostOptimisation_fleet_demo_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Fleet cost optimisation/0 fleet demo.lingo");

		runScenario(url);
	}

	@Test
	public void testFleetCostOptimisation_fleet_demo_VerticalReport() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Fleet cost optimisation/0 fleet demo.lingo");
		testReports(url, ReportTester.VERTICAL_REPORT_ID, "html");
	}

	@Test
	public void testFleetCostOptimisation_fleet_demo_ScheduleSummary() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Fleet cost optimisation/0 fleet demo.lingo");
		testReports(url, ReportTester.SCHEDULE_SUMMARY_ID, "html");
	}

	@Test
	public void testFleetCostOptimisation_fleet_demo_PortRotations() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Fleet cost optimisation/0 fleet demo.lingo");
		testReports(url, ReportTester.PORT_ROTATIONS_ID, "html");
	}

	@Test
	public void testFleetCostOptimisation_O_fleet_demo_Optimise() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Fleet cost optimisation/1 -O- fleet demo.lingo");

		runScenario(url);
	}

	@Test
	public void testFleetCostOptimisation_O_fleet_demo_VerticalReport() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Fleet cost optimisation/1 -O- fleet demo.lingo");
		testReports(url, ReportTester.VERTICAL_REPORT_ID, "html");
	}

	@Test
	public void testFleetCostOptimisation_O_fleet_demo_ScheduleSummary() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Fleet cost optimisation/1 -O- fleet demo.lingo");
		testReports(url, ReportTester.SCHEDULE_SUMMARY_ID, "html");
	}

	@Test
	public void testFleetCostOptimisation_O_fleet_demo_PortRotations() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Fleet cost optimisation/1 -O- fleet demo.lingo");
		testReports(url, ReportTester.PORT_ROTATIONS_ID, "html");
	}

}
