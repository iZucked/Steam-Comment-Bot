/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.csv.scenarios;

import java.net.MalformedURLException;
import java.util.Arrays;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.mmxlabs.lingo.its.tests.AbstractReportTester_CSV;

@RunWith(value = Parameterized.class)
public class TestOptimisationResultReports extends AbstractReportTester_CSV {

	public TestOptimisationResultReports(final String name, final String scenario) throws MalformedURLException {
		super(name, scenario);
	}

	@Parameters(name = "{0}")
	public static Iterable<Object[]> generateTests() {
		return Arrays.asList(new Object[][] { { "sample-data", "/scenarios/sample-data/" }, { "des-cargo", "/scenarios/des-cargo/" } });
	}

}
