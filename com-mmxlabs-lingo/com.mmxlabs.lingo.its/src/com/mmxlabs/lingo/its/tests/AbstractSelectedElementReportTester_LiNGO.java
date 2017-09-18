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
 * Abstract class to run parameterised tests on report generation which require a selected element. Sub classes should create a method similar to the one below to run test cases. May need to also
 * include the @RunWith annotation.
 * 
 * <pre>
 * &#64;Parameters(name = "{0}")
 * 	public static Iterable<Object[]> generateTests() {
 * 		return Arrays.asList(new Object[][] {
 * 				{ "Test Prefix", "scenario path.lingo" , "Element ID" }, //
 * 				{ "Test Prefix", "scenario path.lingo", "Element ID" }  //
 * 		);
 * 	}
 * </pre>
 * 
 * 
 */
@RunWith(value = Parameterized.class)
public abstract class AbstractSelectedElementReportTester_LiNGO extends AbstractSelectedElementReportTester {

	private final Pair<String, String> key;

	private final String elementID;

	public AbstractSelectedElementReportTester_LiNGO(final String name, final String scenarioPath, String elementID) throws Exception {

		this.elementID = elementID;
		key = new Pair<>(name, scenarioPath);
	}

	@Override
	protected void testReports(final String reportID, final String shortName, final String extension, @Nullable Consumer<ScenarioModelRecord> preAction) throws Exception {
		final URL url = getClass().getResource(key.getSecond());

		ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(url, (modelRecord, scenarioDataProvider) -> {
			ReportTester.testReportsWithElement(modelRecord, scenarioDataProvider, url, reportID, shortName, extension, elementID, preAction);
		});
	}
}
