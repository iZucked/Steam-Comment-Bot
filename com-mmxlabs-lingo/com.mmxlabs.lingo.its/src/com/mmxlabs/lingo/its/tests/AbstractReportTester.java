/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.text.rtf.RTFEditorKit;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

/**
 * Abstract class to run parameterised tests on report generation. Sub classes
 * should create a method similar to the one below to run test cases. May need
 * to also include the @RunWith annotation.
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

	public enum ReportType {
		REPORT_JSON("json"), REPORT_HTML("html");

		private final String ext;

		private ReportType(String ext) {
			this.ext = ext;
		}

		public String getExtension() {
			return ext;
		}
	}

	protected abstract void init(final String scenarioPath) throws Exception;

	protected void testReports(final String reportID, final String shortName, final ReportType type) throws Exception {
		testReports(reportID, shortName, type, null);
	}

	protected abstract void testReports(final String reportID, final String shortName, final ReportType type, @Nullable Consumer<ScenarioModelRecord> preAction) throws Exception;

	public List<DynamicNode> makeTests(@NonNull final String scenarioURL) {
		final List<DynamicNode> tests = new LinkedList<>();

		tests.add(DynamicTest.dynamicTest("VerticalReport", () -> {
			Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);
			init(scenarioURL);
			testReports(ReportTesterHelper.VERTICAL_REPORT_ID, ReportTesterHelper.VERTICAL_REPORT_SHORTNAME, ReportType.REPORT_HTML);
		}));
		tests.add(DynamicTest.dynamicTest("ScheduleSummary", () -> {
			Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);
			init(scenarioURL);
			testReports(ReportTesterHelper.SCHEDULE_SUMMARY_ID, ReportTesterHelper.SCHEDULE_SUMMARY_SHORTNAME, ReportType.REPORT_JSON);
		}));
		tests.add(DynamicTest.dynamicTest("PortRotations", () -> {
			Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);
			init(scenarioURL);
			testReports(ReportTesterHelper.PORT_ROTATIONS_ID, ReportTesterHelper.PORT_ROTATIONS_SHORTNAME, ReportType.REPORT_JSON);
		}));
		tests.add(DynamicTest.dynamicTest("LatenessReport", () -> {
			Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);
			init(scenarioURL);
			testReports(ReportTesterHelper.LATENESS_REPORT_ID, ReportTesterHelper.LATENESS_REPORT_SHORTNAME, ReportType.REPORT_JSON);
		}));
		tests.add(DynamicTest.dynamicTest("CapacityReport", () -> {
			Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);
			init(scenarioURL);
			testReports(ReportTesterHelper.CAPACITY_REPORT_ID, ReportTesterHelper.CAPACITY_REPORT_SHORTNAME, ReportType.REPORT_JSON);
		}));
		tests.add(DynamicTest.dynamicTest("VesselReport", () -> {
			Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);
			init(scenarioURL);
			testReports(ReportTesterHelper.VESSEL_REPORT_ID, ReportTesterHelper.VESSEL_REPORT_SHORTNAME, ReportType.REPORT_JSON);
		}));
		tests.add(DynamicTest.dynamicTest("CooldownReport", () -> {
			Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);
			init(scenarioURL);
			testReports(ReportTesterHelper.COOLDOWN_REPORT_ID, ReportTesterHelper.COOLDOWN_REPORT_SHORTNAME, ReportType.REPORT_JSON);
		}));
		tests.add(DynamicTest.dynamicTest("HeadlineReport", () -> {
			Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);
			init(scenarioURL);
			testReports(ReportTesterHelper.HEADLINE_REPORT_ID, ReportTesterHelper.HEADLINE_REPORT_SHORTNAME, ReportType.REPORT_JSON);
		}));
		tests.add(DynamicTest.dynamicTest("KPIReport", () -> {
			Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);
			init(scenarioURL);
			testReports(ReportTesterHelper.KPI_REPORT_ID, ReportTesterHelper.KPI_REPORT_SHORTNAME, ReportType.REPORT_JSON);
		}));
		tests.add(DynamicTest.dynamicTest("CanalBookingsReport", () -> {
			Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);
			init(scenarioURL);
			testReports(ReportTesterHelper.CANAL_BOOKINGS_REPORT_ID, ReportTesterHelper.CANAL_BOOKINGS_REPORT_SHORTNAME, ReportType.REPORT_JSON);
		}));
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_EXPOSURES)) {
			tests.add(DynamicTest.dynamicTest("ExposuresReport", () -> {
				Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);

				init(scenarioURL);
				testReports(ReportTesterHelper.EXPOSURES_REPORT_ID, ReportTesterHelper.EXPOSURES_REPORT_SHORTNAME, ReportType.REPORT_HTML);
			}));
		}
		tests.add(DynamicTest.dynamicTest("IncomeStatementRegion", () -> {
			Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);
			init(scenarioURL);
			testReports(ReportTesterHelper.INCOME_STATEMENT_REGION_REPORT_ID, ReportTesterHelper.INCOME_STATEMENT_REGION_REPORT_SHORTNAME, ReportType.REPORT_JSON);
		}));
		tests.add(DynamicTest.dynamicTest("IncomeStatementContract", () -> {
			Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);

			init(scenarioURL);
			testReports(ReportTesterHelper.INCOME_STATEMENT_CONTRACT_REPORT_ID, ReportTesterHelper.INCOME_STATEMENT_CONTRACT_REPORT_SHORTNAME, ReportType.REPORT_JSON);
		}));

		return tests;
	}
}
