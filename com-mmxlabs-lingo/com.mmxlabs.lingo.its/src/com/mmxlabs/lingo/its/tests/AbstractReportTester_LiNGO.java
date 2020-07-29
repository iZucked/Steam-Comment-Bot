/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
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
 * 				{ "Test Prefix", "scenario path.lingo" }, //
 * 				{ "Test Prefix", "scenario path.lingo" }  //
 * 		);
 * 	}
 * </pre>
 * 
 * 
 */
public abstract class AbstractReportTester_LiNGO extends AbstractReportTester {

	private static Map<Pair<String, String>, Triple<URL, ScenarioModelRecord, IScenarioDataProvider>> cache = new HashMap<>();

	private Pair<String, String> key;

	@Override
	public void init(final String scenarioPath) throws Exception {

		// Note: Junit 4.1.3 or Junit 5 should be able to do this without need for cache.
		// JUnit 4.1.3 will have @Before/AfterParam annotations
		// Currently we only unload scenarios once ALL report/scenario test combinations in class have run - potential memory leak
		key = new Pair<>(scenarioPath, scenarioPath);
		if (TestingModes.ReportTestMode != TestMode.Skip) {

			if (!cache.containsKey(key)) {
				final URL url = getClass().getResource(scenarioPath);

				final Pair<ScenarioModelRecord, IScenarioDataProvider> p = ScenarioStorageUtil.loadFromResourceURL(url);
				final ScenarioModelRecord modelRecord = p.getFirst();
				final IScenarioDataProvider scenarioDataProvider = p.getSecond();

				Assertions.assertNotNull(url);
				Assertions.assertNotNull(modelRecord);
				Assertions.assertNotNull(scenarioDataProvider);
				// A side-effect is the initial evaluation.
				LNGScenarioRunnerCreator.withLegacyEvaluationRunner(scenarioDataProvider, true, runner -> {
				});

				cache.put(key, new Triple<>(url, modelRecord, scenarioDataProvider));
			}
		}
	}

	@AfterAll
	public static void clearCache() throws Exception {

		final Iterator<Map.Entry<Pair<String, String>, Triple<URL, ScenarioModelRecord, IScenarioDataProvider>>> itr = cache.entrySet().iterator();
		while (itr.hasNext()) {
			final Map.Entry<Pair<String, String>, Triple<URL, ScenarioModelRecord, IScenarioDataProvider>> e = itr.next();
			final Triple<URL, ScenarioModelRecord, IScenarioDataProvider> value = e.getValue();
			if (value != null) {
				value.getThird().close();
			}
			itr.remove();
		}
	}

	@Override
	protected void testReports(final String reportID, final String shortName, final ReportType type, @Nullable final Consumer<ScenarioModelRecord> preAction) throws Exception {
		Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);

		final Triple<URL, ScenarioModelRecord, IScenarioDataProvider> triple = cache.get(key);

		Assertions.assertNotNull(triple);

		final URL url = triple.getFirst();
		final ScenarioModelRecord modelRecord = triple.getSecond();
		final IScenarioDataProvider scenarioDataProvider = triple.getThird();

		Assertions.assertNotNull(url);
		Assertions.assertNotNull(modelRecord);
		Assertions.assertNotNull(scenarioDataProvider);

		ReportTester.testReports_NoEvaluate(modelRecord, scenarioDataProvider, url, reportID, shortName, type, preAction);
	}
}
