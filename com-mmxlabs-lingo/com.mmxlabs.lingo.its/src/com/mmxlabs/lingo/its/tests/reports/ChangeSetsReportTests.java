/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.reports;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.lingo.its.tests.ReportTester;
import com.mmxlabs.lingo.its.tests.ReportTesterHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ChangeSetsReportTests extends AbstractOptimisationResultTester {

	// NOTE: This does not work - it is applied too late in the application life-cycle. Instead we need to enable it in the ITS feature.
	private static List<String> requiredFeatures = Lists.newArrayList("difftools");
	private static List<String> addedFeatures = new LinkedList<>();

	@BeforeClass
	public static void hookIn() {
		for (final String feature : requiredFeatures) {
			if (!LicenseFeatures.isPermitted("features:" + feature)) {
				LicenseFeatures.addFeatureEnablements(feature);
				addedFeatures.add(feature);
			}
		}
	}

	@AfterClass
	public static void hookOut() {
		for (final String feature : addedFeatures) {
			LicenseFeatures.removeFeatureEnablements(feature);
		}
		addedFeatures.clear();
	}

	
	@Test
	public void testBonnyProblemsDiff() throws Exception {
		final URL pinScenarioURL = getClass().getResource("/scenarios/demo-cases/Bonny problems/0 Bonny.lingo");
		final URL refScenarioURL = getClass().getResource("/scenarios/demo-cases/Bonny problems/2 -O- late and lost-DES backfill -F- Bonny.lingo");
		final ScenarioInstance pinInstance = loadScenario(pinScenarioURL);
		Assert.assertNotNull(pinInstance);
		Assert.assertNotNull(pinInstance.getInstance());

		final ScenarioInstance refInstance = loadScenario(refScenarioURL);
		Assert.assertNotNull(refInstance);
		Assert.assertNotNull(refInstance.getInstance());

		ReportTester.testPinDiffReports(pinInstance, refInstance, pinScenarioURL, ReportTesterHelper.CHANGESET_REPORT_ID, ReportTesterHelper.CHANGESET_REPORT_SHORTNAME, "html");
	}

	@Test
	public void testDrydockDiff() throws Exception {
		final URL pinScenarioURL = getClass().getResource("/scenarios/demo-cases/Dry dock issues/0 base.lingo");
		final URL refScenarioURL = getClass().getResource("/scenarios/demo-cases/Dry dock issues/5 charter-in generated - rewire for shorter one -O- -F- -F- base.lingo");
		final ScenarioInstance pinInstance = loadScenario(pinScenarioURL);
		Assert.assertNotNull(pinInstance);
		Assert.assertNotNull(pinInstance.getInstance());

		final ScenarioInstance refInstance = loadScenario(refScenarioURL);
		Assert.assertNotNull(refInstance);
		Assert.assertNotNull(refInstance.getInstance());

		ReportTester.testPinDiffReports(pinInstance, refInstance, pinScenarioURL, ReportTesterHelper.CHANGESET_REPORT_ID, ReportTesterHelper.CHANGESET_REPORT_SHORTNAME, "html");
	}
}
