/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Assert;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.common.util.URI;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.lingo.its.tests.category.QuickTest;
import com.mmxlabs.lingo.its.utils.MigrationHelper;
import com.mmxlabs.scenario.service.manifest.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Abstract class to run parameterised tests on report generation. Sub classes should create a method similar to the one below to run test cases. May need to also include the @RunWith annotation.
 * 
 * <pre>
 * @Parameters(name = "{0}")
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
public abstract class AbstractReportTester_LiNGO extends AbstractOptimisationResultTester {

	private static Map<Pair<String, String>, Triple<URL, ScenarioInstance, File>> cache = new HashMap<>();

	private final Pair<String, String> key;

	public AbstractReportTester_LiNGO(final String name, final String scenarioPath) throws Exception {

		key = new Pair<>(name, scenarioPath);
		if (!cache.containsKey(key)) {
			final URL url = getClass().getResource(scenarioPath);
			final URI uri = URI.createURI(FileLocator.toFileURL(url).toString().replaceAll(" ", "%20"));
			final ScenarioInstance instance = ScenarioStorageUtil.loadInstanceFromURI(uri, getScenarioCipherProvider());
			Assert.assertNotNull(instance);
			final File f = MigrationHelper.migrateAndLoad(instance);
			Assert.assertNotNull(instance.getInstance());
			cache.put(key, new Triple<>(url, instance, f));
		}
	}

	@AfterClass
	public static void clearCache() throws Exception {

		final Iterator<Map.Entry<Pair<String, String>, Triple<URL, ScenarioInstance, File>>> itr = cache.entrySet().iterator();
		while (itr.hasNext()) {
			final Map.Entry<Pair<String, String>, Triple<URL, ScenarioInstance, File>> e = itr.next();
			itr.remove();
			final File f = e.getValue().getThird();
			if (f != null && f.exists()) {
				f.delete();
			}
		}
	}

	protected void testReports(final String reportID, final String shortName, final String extension) throws Exception {
		Assert.assertTrue(cache.containsKey(key));

		final Triple<URL, ScenarioInstance, File> triple = cache.get(key);
		final URL url = triple.getFirst();
		Assert.assertNotNull(url);

		final ScenarioInstance instance = triple.getSecond();
		Assert.assertNotNull(instance);
		Assert.assertNotNull(instance.getInstance());
		testReports(instance, url, reportID, shortName, extension);
	}

	@Test
	@Category(QuickTest.class)
	public void testVerticalReport() throws Exception {
		testReports(ReportTester.VERTICAL_REPORT_ID, ReportTester.VERTICAL_REPORT_SHORTNAME, "html");
	}

	@Test
	@Category(QuickTest.class)
	public void testScheduleSummary() throws Exception {
		testReports(ReportTester.SCHEDULE_SUMMARY_ID, ReportTester.SCHEDULE_SUMMARY_SHORTNAME, "html");
	}

	@Test
	@Category(QuickTest.class)
	public void testPortRotations() throws Exception {
		testReports(ReportTester.PORT_ROTATIONS_ID, ReportTester.PORT_ROTATIONS_SHORTNAME, "html");
	}

	@Test
	@Category(QuickTest.class)
	public void testLatenessReport() throws Exception {
		testReports(ReportTester.LATENESS_REPORT_ID, ReportTester.LATENESS_REPORT_SHORTNAME, "html");
	}

	@Test
	@Category(QuickTest.class)
	public void testCapacityReport() throws Exception {
		testReports(ReportTester.CAPACITY_REPORT_ID, ReportTester.CAPACITY_REPORT_SHORTNAME, "html");
	}

	@Test
	@Category(QuickTest.class)
	public void testVesselReport() throws Exception {
		testReports(ReportTester.VESSEL_REPORT_ID, ReportTester.VESSEL_REPORT_SHORTNAME, "html");
	}

	@Test
	@Category(QuickTest.class)
	public void testCooldownReport() throws Exception {
		testReports(ReportTester.COOLDOWN_REPORT_ID, ReportTester.COOLDOWN_REPORT_SHORTNAME, "html");
	}
}
