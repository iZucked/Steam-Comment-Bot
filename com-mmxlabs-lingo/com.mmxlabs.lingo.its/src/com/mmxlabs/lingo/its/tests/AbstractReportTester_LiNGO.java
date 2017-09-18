/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.net.URL;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

/**
 * Abstract class to run parameterised tests on report generation. Sub classes should create a method similar to the one below to run test cases. May need to also include the @RunWith annotation.
 * 
 * <pre>
 * &#64;Parameters(name = "{0}")
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
public abstract class AbstractReportTester_LiNGO extends AbstractReportTester {

	private final Pair<String, String> key;

	public AbstractReportTester_LiNGO(final String name, final String scenarioPath) throws Exception {

		key = new Pair<>(name, scenarioPath);
	}

	@Override
	protected void testReports(final String reportID, final String shortName, final String extension, @Nullable final Consumer<ScenarioModelRecord> preAction) throws Exception {
		final URL url = getClass().getResource(key.getSecond());

		ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(url, (modelRecord, scenarioDataProvider) -> {
			ReportTester.testReports(modelRecord, scenarioDataProvider, url, reportID, shortName, extension, preAction);
			scenarioDataProvider.close();
		});
	}
}
