/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Abstract class to run parameterised tests on report generation. Sub classes should create a method similar to the one below to run test cases. May need to also include the @RunWith annotation.
 * 
 * <pre>
 * @Parameters(name = "{0}")
 * 	public static Iterable<Object[]> generateTests() {
 * 		return Arrays.asList(new Object[][] {
 * 				{ "Test Prefix", "scenario path.lingo" }, //
 * 				{ "Test Prefix", "scenario path.lingo" }  //
 * 		);
 * 	}
 * </pre>
 * 
 * 
 */
@RunWith(value = Parameterized.class)
public abstract class AbstractReportTester_LiNGO extends AbstractOptimisationResultTester {

	// Used by annotation before getting to constructor
	@SuppressWarnings("unused")
	private final String name;
	private final String scenario;

	public AbstractReportTester_LiNGO(final String name, final String scenario) {
		this.name = name;
		this.scenario = scenario;
	}

	@Test
	public void reportTest_VerticalReport() throws Exception {
		final URL url = getClass().getResource(scenario);
		testReports(url, ReportTester.VERTICAL_REPORT_ID, ReportTester.VERTICAL_REPORT_SHORTNAME, "html");
	}

	@Test
	public void reportTest_ScheduleSummary() throws Exception {
		final URL url = getClass().getResource(scenario);
		testReports(url, ReportTester.SCHEDULE_SUMMARY_ID, ReportTester.SCHEDULE_SUMMARY_SHORTNAME, "html");
	}

	@Test
	public void reportTest_PortRotations() throws Exception {
		final URL url = getClass().getResource(scenario);
		testReports(url, ReportTester.PORT_ROTATIONS_ID, ReportTester.PORT_ROTATIONS_SHORTNAME, "html");
	}
}
