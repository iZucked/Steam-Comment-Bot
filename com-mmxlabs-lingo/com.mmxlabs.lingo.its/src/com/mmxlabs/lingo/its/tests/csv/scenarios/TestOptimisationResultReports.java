/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.csv.scenarios;

import java.util.Arrays;

import org.junit.runners.Parameterized.Parameters;

import com.mmxlabs.lingo.its.tests.AbstractReportTester_CSV;

public class TestOptimisationResultReports extends AbstractReportTester_CSV {

	public TestOptimisationResultReports(final String name, final String scenario) {
		super(name, scenario);
	}

	@Parameters(name = "{0}")
	public static Iterable<Object[]> generateTests() {
		return Arrays.asList(new Object[][] { { "sample-data", "/scenarios/sample-data/" }, { "des-cargo", "/scenarios/des-cargo/" } });
	}

}
