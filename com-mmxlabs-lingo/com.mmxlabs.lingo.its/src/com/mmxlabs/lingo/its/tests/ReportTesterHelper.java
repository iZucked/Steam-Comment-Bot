/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.E4PartWrapper;
import org.junit.jupiter.api.Assertions;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.its.tests.AbstractReportTester.ReportType;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.ReportsConstants;
import com.mmxlabs.lingo.reports.views.fleet.ConfigurableVesselSummaryReport;
import com.mmxlabs.lingo.reports.views.headline.HeadlineReportView;
import com.mmxlabs.lingo.reports.views.portrotation.PortRotationReportView;
import com.mmxlabs.lingo.reports.views.schedule.ScheduleSummaryReport;
import com.mmxlabs.lingo.reports.views.standard.CooldownReportView;
import com.mmxlabs.lingo.reports.views.standard.KPIReportView;
import com.mmxlabs.lingo.reports.views.standard.LatenessReportView;
import com.mmxlabs.lingo.reports.views.standard.VolumeIssuesReportView;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.IScenarioServiceSelectionProvider;

/**
 * Helper class to open up a view, set the scenario selection provider to the
 * given instance and adapt the result to a {@link IReportContents} instance.
 * 
 * @author Simon Goodall
 * 
 */
public class ReportTesterHelper {

	public static record ReportRecord(String reportID, String fileNameCode, ReportType reportType) {
	}

	public static final String SCHEDULE_SUMMARY_ID = ScheduleSummaryReport.ID;
	public static final String SCHEDULE_SUMMARY_SHORTNAME = "ScheduleSummary";

	public static final String PORT_ROTATIONS_ID = PortRotationReportView.ID;
	public static final String PORT_ROTATIONS_SHORTNAME = "PortRotations";

	public static final String VESSEL_REPORT_ID = ConfigurableVesselSummaryReport.ID;
	public static final String VESSEL_REPORT_SHORTNAME = "VesselReport";

	public static final String VERTICAL_REPORT_ID = "com.mmxlabs.lingo.reports.verticalreport";
	public static final String VERTICAL_REPORT_SHORTNAME = "VerticalReport";

	public static final String LATENESS_REPORT_ID = LatenessReportView.ID;
	public static final String LATENESS_REPORT_SHORTNAME = "LatenessReport";

	public static final String CAPACITY_REPORT_ID = VolumeIssuesReportView.ID;
	public static final String CAPACITY_REPORT_SHORTNAME = "CapacityReport";

	public static final String COOLDOWN_REPORT_ID = CooldownReportView.ID;
	public static final String COOLDOWN_REPORT_SHORTNAME = "CooldownReport";

	public static final String HEADLINE_REPORT_ID = HeadlineReportView.ID;
	public static final String HEADLINE_REPORT_SHORTNAME = "HeadlineReport";

	public static final String KPI_REPORT_ID = KPIReportView.ID;
	public static final String KPI_REPORT_SHORTNAME = "KPIReport";

	public static final String CHANGESET_REPORT_ID = ReportsConstants.VIEW_COMPARE_SCENARIOS_ID;
	public static final String CHANGESET_REPORT_SHORTNAME = "ChangeSetReport";

	public static final String ACTIONPLAN_REPORT_ID = ReportsConstants.VIEW_COMPARE_SCENARIOS_ID;
	public static final String ACTIONPLAN_REPORT_SHORTNAME = "ActionPlanReport";

	public static final String EXPOSURES_REPORT_ID = "com.mmxlabs.shiplingo.platform.reports.views.ExposureReportView";
	public static final String EXPOSURES_REPORT_SHORTNAME = "ExposuresReport";
	
	public static final String EXPOSURE_DETAILS_REPORT_ID = "com.mmxlabs.shiplingo.platform.reports.views.ExposureDetailReportView";
	public static final String EXPOSURE_DETAILS_REPORT_SHORTNAME = "ExposureDetailsReport";

	public static final String CARGO_ECONS_REPORT_ID = "com.mmxlabs.shiplingo.platform.reports.views.CargoEconsReport";
	public static final String CARGO_ECONS_REPORT_SHORTNAME = "CargoEcons";

	public static final String CANAL_BOOKINGS_REPORT_ID = "com.mmxlabs.lingo.reports.views.standard.CanalBookingsReport";
	public static final String CANAL_BOOKINGS_REPORT_SHORTNAME = "CanalBookings";

	public static final String INCOME_STATEMENT_REGION_REPORT_ID = "com.mmxlabs.lingo.reports.views.standard.incomestatement.IncomeStatementByRegion";
	public static final String INCOME_STATEMENT_REGION_REPORT_SHORTNAME = "IncomeStatementRegion";

	public static final String INCOME_STATEMENT_CONTRACT_REPORT_ID = "com.mmxlabs.lingo.reports.views.standard.incomestatement.IncomeStatementByContract";
	public static final String INCOME_STATEMENT_CONTRACT_REPORT_SHORTNAME = "IncomeStatementContract";

	public static List<ReportRecord> createReportTests() {
		final List<ReportRecord> reports = new LinkedList<>();

		reports.add(new ReportRecord(ReportTesterHelper.VERTICAL_REPORT_ID, ReportTesterHelper.VERTICAL_REPORT_SHORTNAME, ReportType.REPORT_HTML));
		reports.add(new ReportRecord(ReportTesterHelper.SCHEDULE_SUMMARY_ID, ReportTesterHelper.SCHEDULE_SUMMARY_SHORTNAME, ReportType.REPORT_JSON));
		reports.add(new ReportRecord(ReportTesterHelper.PORT_ROTATIONS_ID, ReportTesterHelper.PORT_ROTATIONS_SHORTNAME, ReportType.REPORT_JSON));
		reports.add(new ReportRecord(ReportTesterHelper.LATENESS_REPORT_ID, ReportTesterHelper.LATENESS_REPORT_SHORTNAME, ReportType.REPORT_JSON));
		reports.add(new ReportRecord(ReportTesterHelper.CAPACITY_REPORT_ID, ReportTesterHelper.CAPACITY_REPORT_SHORTNAME, ReportType.REPORT_JSON));
		reports.add(new ReportRecord(ReportTesterHelper.VESSEL_REPORT_ID, ReportTesterHelper.VESSEL_REPORT_SHORTNAME, ReportType.REPORT_JSON));
		reports.add(new ReportRecord(ReportTesterHelper.COOLDOWN_REPORT_ID, ReportTesterHelper.COOLDOWN_REPORT_SHORTNAME, ReportType.REPORT_JSON));
		reports.add(new ReportRecord(ReportTesterHelper.HEADLINE_REPORT_ID, ReportTesterHelper.HEADLINE_REPORT_SHORTNAME, ReportType.REPORT_JSON));
		reports.add(new ReportRecord(ReportTesterHelper.KPI_REPORT_ID, ReportTesterHelper.KPI_REPORT_SHORTNAME, ReportType.REPORT_JSON));
		reports.add(new ReportRecord(ReportTesterHelper.CANAL_BOOKINGS_REPORT_ID, ReportTesterHelper.CANAL_BOOKINGS_REPORT_SHORTNAME, ReportType.REPORT_JSON));
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_EXPOSURES)) {
			reports.add(new ReportRecord(ReportTesterHelper.EXPOSURES_REPORT_ID, ReportTesterHelper.EXPOSURES_REPORT_SHORTNAME, ReportType.REPORT_HTML));
		}
		reports.add(new ReportRecord(ReportTesterHelper.INCOME_STATEMENT_REGION_REPORT_ID, ReportTesterHelper.INCOME_STATEMENT_REGION_REPORT_SHORTNAME, ReportType.REPORT_JSON));
		reports.add(new ReportRecord(ReportTesterHelper.INCOME_STATEMENT_CONTRACT_REPORT_ID, ReportTesterHelper.INCOME_STATEMENT_CONTRACT_REPORT_SHORTNAME, ReportType.REPORT_JSON));

		return reports;
	}

	public static List<ReportRecord> createSelectedElementReportTests() {
		final List<ReportRecord> reports = new LinkedList<>();
		reports.add(new ReportRecord(ReportTesterHelper.CARGO_ECONS_REPORT_ID, ReportTesterHelper.CARGO_ECONS_REPORT_SHORTNAME, ReportType.REPORT_JSON));
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_EXPOSURES)) {
			reports.add(new ReportRecord(ReportTesterHelper.EXPOSURE_DETAILS_REPORT_ID, ReportTesterHelper.EXPOSURE_DETAILS_REPORT_SHORTNAME, ReportType.REPORT_HTML));
		}

		return reports;
	}

	@FunctionalInterface
	interface IScenarioSelection {
		void updateSelection(IViewPart view, IScenarioServiceSelectionProvider provider);
	}

	public <U> void runReportTest(final String reportID, final @Nullable IScenarioSelection callable, final @Nullable IScenarioSelection cleanUp, final Class<U> adapterClass, final Consumer<U> consumer)
			throws InterruptedException {

		// Get reference to the selection provider service

		ServiceHelper.withCheckedServiceConsumer(IScenarioServiceSelectionProvider.class, provider -> {

			final IViewPart[] view = new IViewPart[1];
			try {
				// Step 1 open the view, release UI thread
				Display.getDefault().syncExec(() -> {
					final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					Assertions.assertNotNull(activePage);
					try {
						// Close the existing view reference. E.g. because we have changed the license
						// features.
						final IViewReference ref = activePage.findViewReference(reportID);
						if (ref != null) {
							// This will dispose the view
							activePage.hideView(ref);
						}

						// Clear existing selection so newly opened view does not pick up any prior
						// data.
						provider.deselectAll(true);

						view[0] = activePage.showView(reportID);
						Assertions.assertNotNull(view[0]);
						activePage.activate(view[0]);

					} catch (final PartInitException e) {
						e.printStackTrace();
					}
				});

				// Step two set the new selection, release UI thread
				Thread.sleep(1000);
				Thread.yield();

				if (callable != null) {
					RunnerHelper.exec(() -> callable.updateSelection(view[0], provider), true);
					Thread.yield();
					Thread.sleep(1000);
				}
				// Step 3, obtain report contents
				Display.getDefault().syncExec(() -> {
					U contents = view[0].getAdapter(adapterClass);
					if (contents == null) {
						if (view[0] instanceof E4PartWrapper) {
							final IViewSite viewSite = view[0].getViewSite();
							final EPartService service = viewSite.getService(EPartService.class);
							final MPart p = service.findPart(reportID);
							if (p != null) {
								final Object o = p.getObject();
								if (o instanceof final IAdaptable adaptable) {
									contents = adaptable.getAdapter(adapterClass);
								}
							}
							// EModelService
							// EPartService
						}
					}
					consumer.accept(contents);
				});
			} finally {
				if (cleanUp != null) {
					RunnerHelper.exec(() -> cleanUp.updateSelection(view[0], provider), true);
				}
			}
		});
	}
}