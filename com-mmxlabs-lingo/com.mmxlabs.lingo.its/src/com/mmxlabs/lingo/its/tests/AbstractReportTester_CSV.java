/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
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
import com.mmxlabs.models.lng.transformer.its.scenario.CSVImporter;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

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
	protected void testReports(final String reportID, final String shortName, final String extension, @Nullable final Consumer<ScenarioModelRecord> preAction) throws Exception {
		final URL url = getClass().getResource(key.getSecond());

		final ScenarioModelRecord modelRecord = ScenarioStorageUtil.createFromCopyOf(url.getPath(), CSVImporter.importCSVScenario(url.toString()));
		try (final IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("AbstractReportTester_CSV")) {
			ReportTester.testReports(modelRecord, scenarioDataProvider, url, reportID, shortName, extension, preAction);
		}
	}
}
