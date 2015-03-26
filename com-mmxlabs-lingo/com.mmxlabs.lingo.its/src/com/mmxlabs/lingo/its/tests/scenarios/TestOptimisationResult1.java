/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.scenarios;

import java.net.URL;

import org.junit.Test;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.lingo.its.tests.ReportTester;

public class TestOptimisationResult1 extends AbstractOptimisationResultTester {

	@Test
	public void testOptimisationResult() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/sample-data.lingo");

		runScenario(url);
	}

	@Test
	public void testOptimisationResult_VerticalReport() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/sample-data.lingo");
		testReports(url, ReportTester.VERTICAL_REPORT_ID, "html");
	}

	@Test
	public void testOptimisationResult_ScheduleSummary() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/sample-data.lingo");
		testReports(url, ReportTester.SCHEDULE_SUMMARY_ID, "html");
	}

	@Test
	public void testOptimisationResult_PortRotations() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/sample-data.lingo");
		testReports(url, ReportTester.PORT_ROTATIONS_ID, "html");
	}

}
