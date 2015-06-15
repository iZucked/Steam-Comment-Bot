/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.its.tests.category.QuickTest;
import com.mmxlabs.lingo.its.utils.CSVImporter;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

/**
 * Abstract class to run parameterised tests on report generation. Sub classes should create a method similar to the one below to run test cases. May need to also include the @RunWith annotation.
 * 
 * <pre>
 * @Parameters(name = "{0}")
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
@RunWith(value = Parameterized.class)
public abstract class AbstractReportTester_CSV extends AbstractOptimisationResultTester {

	private static Map<Pair<String, String>, Pair<URL, LNGScenarioModel>> cache = new HashMap<>();

	private final Pair<String, String> key;

	public AbstractReportTester_CSV(final String name, final String scenarioPath) {

		key = new Pair<>(name, scenarioPath);
		if (!cache.containsKey(key)) {
			final URL url = getClass().getResource(scenarioPath);
			final LNGScenarioModel scenarioModel = CSVImporter.importCSVScenario(url.toString());
			cache.put(key, new Pair<>(url, scenarioModel));
		}
	}

	protected void testReports(final String reportID, final String shortName, final String extension) throws Exception {
		final Pair<URL, LNGScenarioModel> pair = cache.get(key);
		final URL url = pair.getFirst();
		final LNGScenarioModel scenarioModel = pair.getSecond();
		testReports(scenarioModel, url, reportID, shortName, extension);
	}

	@Test
	public void reportTest_VerticalReport() throws Exception {
		testReports(ReportTester.VERTICAL_REPORT_ID, ReportTester.VERTICAL_REPORT_SHORTNAME, "html");
	}

	@Test
	public void reportTest_ScheduleSummary() throws Exception {
		testReports(ReportTester.SCHEDULE_SUMMARY_ID, ReportTester.SCHEDULE_SUMMARY_SHORTNAME, "html");
	}

	@Test
	public void reportTest_PortRotations() throws Exception {
		testReports(ReportTester.PORT_ROTATIONS_ID, ReportTester.PORT_ROTATIONS_SHORTNAME, "html");
	}

	@Test
	public void testLatenessReport() throws Exception {
		testReports(ReportTester.LATENESS_REPORT_ID, ReportTester.LATENESS_REPORT_SHORTNAME, "html");
	}

	@Test
	public void testCapacityReport() throws Exception {
		testReports(ReportTester.CAPACITY_REPORT_ID, ReportTester.CAPACITY_REPORT_SHORTNAME, "html");
	}

	@Test
	public void testVesselReport() throws Exception {
		testReports(ReportTester.VESSEL_REPORT_ID, ReportTester.VESSEL_REPORT_SHORTNAME, "html");
	}

	@Test
	public void testCooldownReport() throws Exception {
		testReports(ReportTester.COOLDOWN_REPORT_ID, ReportTester.COOLDOWN_REPORT_SHORTNAME, "html");
	}
}
