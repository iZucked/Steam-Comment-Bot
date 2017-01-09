/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.util.function.Consumer;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Assume;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.its.tests.category.ReportTest;
import com.mmxlabs.models.lng.commercial.parseutils.Exposures;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.model.ModelReference;
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
public abstract class AbstractReportTester extends AbstractOptimisationResultTester {

	protected void testReports(final String reportID, final String shortName, final String extension) throws Exception {
		testReports(reportID, shortName, extension, null);
	}
	protected abstract void testReports(final String reportID, final String shortName, final String extension, @Nullable Consumer<ScenarioInstance> preAction) throws Exception;

	@Test
	@Category(ReportTest.class)
	public void reportTest_VerticalReport() throws Exception {
		testReports(ReportTesterHelper.VERTICAL_REPORT_ID, ReportTesterHelper.VERTICAL_REPORT_SHORTNAME, "html");
	}

	@Test
	@Category(ReportTest.class)
	public void reportTest_ScheduleSummary() throws Exception {
		testReports(ReportTesterHelper.SCHEDULE_SUMMARY_ID, ReportTesterHelper.SCHEDULE_SUMMARY_SHORTNAME, "html");
	}

	@Test
	@Category(ReportTest.class)
	public void reportTest_PortRotations() throws Exception {
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

	@Test
	@Category(ReportTest.class)
	public void testHeadlineReport() throws Exception {
		testReports(ReportTesterHelper.HEADLINE_REPORT_ID, ReportTesterHelper.HEADLINE_REPORT_SHORTNAME, "html");
	}

	@Test
	@Category(ReportTest.class)
	public void testKPIReport() throws Exception {
		testReports(ReportTesterHelper.KPI_REPORT_ID, ReportTesterHelper.KPI_REPORT_SHORTNAME, "html");
	}

	@Test
	@Category(ReportTest.class)
	public void testExposuresReport() throws Exception {
		Assume.assumeTrue(LicenseFeatures.isPermitted("features:exposures"));
		testReports(ReportTesterHelper.EXPOSURES_REPORT_ID, ReportTesterHelper.EXPOSURES_REPORT_SHORTNAME, "html", instance-> {
			try (ModelReference ref = instance.getReference("ExposuresReportView")) {

				final LNGScenarioModel scenarioModel = (LNGScenarioModel) ref.getInstance();
				final EditingDomain domain = (EditingDomain) instance.getAdapters().get(EditingDomain.class);
				final Schedule schedule = ScenarioModelUtil.getScheduleModel(scenarioModel).getSchedule();
				Exposures.calculateExposures(scenarioModel, schedule, domain);
			}
		});
	}
}
