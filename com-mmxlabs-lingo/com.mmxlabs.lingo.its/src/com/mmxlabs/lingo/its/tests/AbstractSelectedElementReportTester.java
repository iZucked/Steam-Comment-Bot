/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.mmxlabs.lingo.its.tests.category.ReportTest;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

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

	protected void testReports(final String reportID, final String shortName, final String extension) throws Exception {
		testReports(reportID, shortName, extension, null);
	}

	protected abstract void testReports(final String reportID, final String shortName, final String extension, @Nullable Consumer<ScenarioInstance> preAction) throws Exception;

	@Test
	@Category(ReportTest.class)
	public void reportTest_CargoEconsReport() throws Exception {
		testReports(ReportTesterHelper.CARGO_ECONS_REPORT_ID, ReportTesterHelper.CARGO_ECONS_REPORT_SHORTNAME, "html");
	}
}