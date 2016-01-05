/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.common.util.URI;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.its.tests.category.ReportTest;
import com.mmxlabs.models.migration.scenario.MigrationHelper;
import com.mmxlabs.scenario.service.manifest.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.util.encryption.IScenarioCipherProvider;

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
public abstract class AbstractReportTester_LiNGO extends AbstractOptimisationResultTester {

	private final Pair<String, String> key;

	public AbstractReportTester_LiNGO(final String name, final String scenarioPath) throws Exception {

		key = new Pair<>(name, scenarioPath);
	}

	protected Pair<URL, ScenarioInstance> load(Pair<String, String> key) throws IOException {
		final URL url = getClass().getResource(key.getSecond());
		final URI uri = URI.createURI(FileLocator.toFileURL(url).toString().replaceAll(" ", "%20"));

		final BundleContext bundleContext = FrameworkUtil.getBundle(AbstractOptimisationResultTester.class).getBundleContext();
		final ServiceReference<IScenarioCipherProvider> serviceReference = bundleContext.getServiceReference(IScenarioCipherProvider.class);
		try {
			final ScenarioInstance instance = ScenarioStorageUtil.loadInstanceFromURI(uri, bundleContext.getService(serviceReference));
			Assert.assertNotNull(instance);

			MigrationHelper.migrateAndLoad(instance);

			Assert.assertNotNull(instance.getInstance());
			return new Pair<>(url, instance);
		} finally {
			bundleContext.ungetService(serviceReference);
		}
	}

	protected void testReports(final String reportID, final String shortName, final String extension) throws Exception {
		final Pair<URL, ScenarioInstance> triple = load(key);
		final URL url = triple.getFirst();
		Assert.assertNotNull(url);

		final ScenarioInstance instance = triple.getSecond();
		Assert.assertNotNull(instance);
		Assert.assertNotNull(instance.getInstance());
		ReportTester.testReports(executorService, instance, url, reportID, shortName, extension);
	}

	@Test
	@Category(ReportTest.class)
	public void testVerticalReport() throws Exception {
		testReports(ReportTesterHelper.VERTICAL_REPORT_ID, ReportTesterHelper.VERTICAL_REPORT_SHORTNAME, "html");
	}

	@Test
	@Category(ReportTest.class)
	public void testScheduleSummary() throws Exception {
		testReports(ReportTesterHelper.SCHEDULE_SUMMARY_ID, ReportTesterHelper.SCHEDULE_SUMMARY_SHORTNAME, "html");
	}

	@Test
	@Category(ReportTest.class)
	public void testPortRotations() throws Exception {
		testReports(ReportTesterHelper.PORT_ROTATIONS_ID, ReportTesterHelper.PORT_ROTATIONS_SHORTNAME, "html");
	}

	@Test
	@Category(ReportTest.class)
	public void testLatenessReport() throws Exception {
		testReports(ReportTesterHelper.LATENESS_REPORT_ID, ReportTesterHelper.LATENESS_REPORT_SHORTNAME, "html");
	}

	@Test
	@Category(ReportTest.class)
	public void testCapacityReport() throws Exception {
		testReports(ReportTesterHelper.CAPACITY_REPORT_ID, ReportTesterHelper.CAPACITY_REPORT_SHORTNAME, "html");
	}

	@Test
	@Category(ReportTest.class)
	public void testVesselReport() throws Exception {
		testReports(ReportTesterHelper.VESSEL_REPORT_ID, ReportTesterHelper.VESSEL_REPORT_SHORTNAME, "html");
	}

	@Test
	@Category(ReportTest.class)
	public void testCooldownReport() throws Exception {
		testReports(ReportTesterHelper.COOLDOWN_REPORT_ID, ReportTesterHelper.COOLDOWN_REPORT_SHORTNAME, "html");
	}
}
