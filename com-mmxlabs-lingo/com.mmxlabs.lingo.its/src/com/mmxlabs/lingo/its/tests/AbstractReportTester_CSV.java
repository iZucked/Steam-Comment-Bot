/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.its.scenario.CSVImporter;
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
@RunWith(value = Parameterized.class)
public abstract class AbstractReportTester_CSV extends AbstractReportTester {

	private static Map<Pair<String, String>, Pair<URL, LNGScenarioModel>> cache = new HashMap<>();

	private final Pair<String, String> key;

	public AbstractReportTester_CSV(final String name, final String scenarioPath) throws MalformedURLException {
		key = new Pair<>(name, scenarioPath);
		if (!cache.containsKey(key)) {
			final URL url = getClass().getResource(scenarioPath);
			final LNGScenarioModel scenarioModel = CSVImporter.importCSVScenario(url.toString());
			cache.put(key, new Pair<>(url, scenarioModel));
		}
	}

	@Override
	protected void testReports(final String reportID, final String shortName, final String extension) throws Exception {
		final Pair<URL, LNGScenarioModel> pair = cache.get(key);
		final URL url = pair.getFirst();
		final LNGScenarioModel scenarioModel = pair.getSecond();
		ScenarioInstance instance = LNGScenarioRunnerCreator.createScenarioInstance(scenarioModel, url);
		ReportTester.testReports(instance, url, reportID, shortName, extension);
	}
}
