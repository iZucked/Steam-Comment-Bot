/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;
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

	private final Pair<String, String> key;

	public AbstractReportTester_CSV(final String name, final String scenarioPath) throws MalformedURLException {
		key = new Pair<>(name, scenarioPath);
	}

	@Override
	protected void testReports(final String reportID, final String shortName, final String extension, @Nullable Consumer<ScenarioInstance> preAction) throws Exception {
		final URL url = getClass().getResource(key.getSecond());
		final LNGScenarioModel scenarioModel = CSVImporter.importCSVScenario(url.toString());
		ScenarioInstance instance = LNGScenarioRunnerCreator.createFromModelInstance(scenarioModel);
		instance.setName(url.getPath());
		ReportTester.testReports(instance, scenarioModel, url, reportID, shortName, extension, preAction);
	}
}
