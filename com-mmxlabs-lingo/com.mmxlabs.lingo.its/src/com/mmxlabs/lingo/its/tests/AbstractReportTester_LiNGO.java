/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

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

	private static Map<Pair<String, String>, Pair<URL, ScenarioInstance>> cache = new HashMap<>();

	private final Pair<String, String> key;

	public AbstractReportTester_LiNGO(final String name, final String scenarioPath) throws Exception {

		key = new Pair<>(name, scenarioPath);
		if (!cache.containsKey(key)) {
			final URL url = getClass().getResource(scenarioPath);
			final ScenarioInstance instance = loadScenario(url);
			Assert.assertNotNull(instance);
			Assert.assertNotNull(instance.getInstance());
			cache.put(key, new Pair<>(url, instance));
		}
	}

	@AfterClass
	public static void clearCache() throws Exception {

		final Iterator<Map.Entry<Pair<String, String>, Pair<URL, ScenarioInstance>>> itr = cache.entrySet().iterator();
		while (itr.hasNext()) {
			final Map.Entry<Pair<String, String>, Pair<URL, ScenarioInstance>> e = itr.next();
			itr.remove();
		}
	}

	@Override
	protected void testReports(final String reportID, final String shortName, final String extension, @Nullable Consumer<ScenarioInstance> preAction) throws Exception {
		Assert.assertTrue(cache.containsKey(key));

		final Pair<URL, ScenarioInstance> triple = cache.get(key);
		final URL url = triple.getFirst();
		Assert.assertNotNull(url);

		final ScenarioInstance instance = triple.getSecond();
		Assert.assertNotNull(instance);
		Assert.assertNotNull(instance.getInstance());

		ReportTester.testReports(instance, url, reportID, shortName, extension, preAction);
	}
}
