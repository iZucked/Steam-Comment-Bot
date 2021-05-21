/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.reports;

import java.net.URL;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.lingo.its.tests.ReportTester;
import com.mmxlabs.lingo.its.tests.ReportTesterHelper;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.transformer.its.RequireFeature;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;

@ExtendWith(ShiroRunner.class)
// NOTE: This does not work - it is applied too late in the application life-cycle. Instead we need to enable it in the ITS feature.
@RequireFeature(KnownFeatures.FEATURE_MODULE_DIFF_TOOLS)
public class ChangeSetsReportTests extends AbstractOptimisationResultTester {

	@Test
	@Tag(TestCategories.REPORT_TEST)
	public void testBonnyProblemsDiff() throws Exception {
		final URL pinScenarioURL = getClass().getResource("/scenarios/demo-cases/Bonny problems/0 Bonny.lingo");
		final URL refScenarioURL = getClass().getResource("/scenarios/demo-cases/Bonny problems/2 -O- late and lost-DES backfill -F- Bonny.lingo");

		// final URL dataURL = getClass().getResource("/scenarios/demo-cases/Bonny problems/");
		final URL dataURL = pinScenarioURL;

		ReportTester.testPinDiffReports(pinScenarioURL, refScenarioURL, dataURL, ReportTesterHelper.CHANGESET_REPORT_ID, ReportTesterHelper.CHANGESET_REPORT_SHORTNAME, "html");
	}

	@Test
	@Tag(TestCategories.REPORT_TEST)
	public void testDrydockDiff() throws Exception {
		final URL pinScenarioURL = getClass().getResource("/scenarios/demo-cases/Dry dock issues/0 base.lingo");
		final URL refScenarioURL = getClass().getResource("/scenarios/demo-cases/Dry dock issues/5 charter-in generated - rewire for shorter one -O- -F- -F- base.lingo");

		// final URL dataURL = getClass().getResource("/scenarios/demo-cases/Dry dock issues/");
		final URL dataURL = pinScenarioURL;

		ReportTester.testPinDiffReports(pinScenarioURL, refScenarioURL, dataURL, ReportTesterHelper.CHANGESET_REPORT_ID, ReportTesterHelper.CHANGESET_REPORT_SHORTNAME, "html");
	}
}
