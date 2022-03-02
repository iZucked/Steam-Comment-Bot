/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.net.URL;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.transformer.its.scenario.ITSCSVImporter;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

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
public abstract class AbstractReportTester_CSV extends AbstractReportTester {

	private Pair<String, String> key;

	@Override
	public void init(final String scenarioPath) throws Exception {
		key = new Pair<>(scenarioPath, scenarioPath);
	}

	@Override
	protected void testReports(final String reportID, final String shortName, final ReportType type, @Nullable final Consumer<ScenarioModelRecord> preAction) throws Exception {
		final URL url = getClass().getResource(key.getSecond());

		final ScenarioModelRecord modelRecord = ScenarioStorageUtil.createFromCopyOf(url.getPath(), new ITSCSVImporter().importCSVScenario(url.toString()));
		try (final IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("AbstractReportTester_CSV")) {
			ReportTester.testReports(modelRecord, scenarioDataProvider, url, reportID, shortName, type, preAction);
		}
	}
}
