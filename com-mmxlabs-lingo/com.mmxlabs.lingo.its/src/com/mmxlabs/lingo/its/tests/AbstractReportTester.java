/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.mmxlabs.lingo.its.tests.category.ReportTest;

/**
 * Abstract class to run parameterised tests on report generation. Sub classes should create a method similar to the one below to run test cases. May need to also include the @RunWith annotation.
 * 
 * <pre>
 * &#64;Parameters(name = "{0}")
 * 	public static Iterable<Object[]> generateTests() {
 * 		return Arrays.asList(new Object[][] {
 * 				{ "Test Prefix", "scenario path/" }, //
 * 				{ "Test Prefix", "scenario path/" }  //
 * 		);
 * 	}
 * </pre>
 * 
 * 
 */
public abstract class AbstractReportTester extends AbstractOptimisationResultTester {

	protected abstract void testReports(final String reportID, final String shortName, final String extension) throws Exception;

	@Test
	@Category(ReportTest.class)
	public void reportTest_VerticalReport() throws Exception {
		testReports(ReportTesterHelper.VERTICAL_REPORT_ID, ReportTesterHelper.VERTICAL_REPORT_SHORTNAME, "html");
	}

	@Test
	@Category(ReportTest.class)
	public void reportTest_ScheduleSummary() throws Exception {
		testReports(ReportTesterHelper.SCHEDULE_SUMMARY_ID, ReportTesterHelper.SCHEDULE_SUMMARY_SHORTNAME, "html");
	}

	@Test
	@Category(ReportTest.class)
	public void reportTest_PortRotations() throws Exception {
		testReports(ReportTesterHelper.PORT_ROTATIONS_ID, ReportTesterHelper.PORT_ROTATIONS_SHORTNAME, "html");
	}

	@Test
	@Category(ReportTest.class)
	public void testLatenessReport() throws Exception {
		testReports(ReportTesterHelper.LATENESS_REPORT_ID, ReportTesterHelper.LATENESS_REPORT_SHORTNAME, "html");
	}

	@Test
	@Category(ReportTest.class)
	public void testCapacityReport() throws Exception {
		testReports(ReportTesterHelper.CAPACITY_REPORT_ID, ReportTesterHelper.CAPACITY_REPORT_SHORTNAME, "html");
	}

	@Test
	@Category(ReportTest.class)
	public void testVesselReport() throws Exception {
		testReports(ReportTesterHelper.VESSEL_REPORT_ID, ReportTesterHelper.VESSEL_REPORT_SHORTNAME, "html");
	}

	@Test
	@Category(ReportTest.class)
	public void testCooldownReport() throws Exception {
		testReports(ReportTesterHelper.COOLDOWN_REPORT_ID, ReportTesterHelper.COOLDOWN_REPORT_SHORTNAME, "html");
	}

	@Test
	@Category(ReportTest.class)
	public void testHeadlineReport() throws Exception {
		testReports(ReportTesterHelper.HEADLINE_REPORT_ID, ReportTesterHelper.HEADLINE_REPORT_SHORTNAME, "html");
	}

	@Test
	@Category(ReportTest.class)
	public void testKPIReport() throws Exception {
		testReports(ReportTesterHelper.KPI_REPORT_ID, ReportTesterHelper.KPI_REPORT_SHORTNAME, "html");
	}
}
