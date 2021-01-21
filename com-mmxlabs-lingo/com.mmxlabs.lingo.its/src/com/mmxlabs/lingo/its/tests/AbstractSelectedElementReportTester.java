/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;

import com.mmxlabs.lingo.its.tests.AbstractReportTester.ReportType;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

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
public abstract class AbstractSelectedElementReportTester extends AbstractOptimisationResultTester {
	public abstract void init(final String scenarioPath, String elementID) throws Exception;

	protected void testReports(final String reportID, final String shortName, final ReportType type) throws Exception {
		testReports(reportID, shortName, type, null);
	}

	protected abstract void testReports(final String reportID, final String shortName, final ReportType type, @Nullable Consumer<ScenarioModelRecord> preAction) throws Exception;

	public List<DynamicNode> makeTests(final String scenarioPath, final String elementID) {
		final List<DynamicNode> tests = new LinkedList<>();

		tests.add(DynamicTest.dynamicTest("CargoEconsReport", () -> {
			Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);
			init(scenarioPath, elementID);
			testReports(ReportTesterHelper.CARGO_ECONS_REPORT_ID, ReportTesterHelper.CARGO_ECONS_REPORT_SHORTNAME, ReportType.REPORT_JSON);
		}));

		return tests;
	}

}