/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.E4PartWrapper;
import org.junit.Assert;

import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.views.IProvideEditorInputScenario;
import com.mmxlabs.lingo.reports.views.changeset.IActionPlanHandler;
import com.mmxlabs.lingo.reports.views.fleet.ConfigurableFleetReportView;
import com.mmxlabs.lingo.reports.views.portrotation.PortRotationReportView;
import com.mmxlabs.lingo.reports.views.schedule.ScheduleSummaryReport;
import com.mmxlabs.lingo.reports.views.standard.CapacityViolationReportView;
import com.mmxlabs.lingo.reports.views.standard.CooldownReportView;
import com.mmxlabs.lingo.reports.views.standard.HeadlineReportView;
import com.mmxlabs.lingo.reports.views.standard.KPIReportView;
import com.mmxlabs.lingo.reports.views.standard.LatenessReportView;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 * Helper class to open up a view, set the scenario selection provider to the given instance and adapt the result to a {@link IReportContents} instance.
 * 
 * @author Simon Goodall
 * 
 */
public class ReportTesterHelper {

	public static final String SCHEDULE_SUMMARY_ID = ScheduleSummaryReport.ID;
	public static final String SCHEDULE_SUMMARY_SHORTNAME = "ScheduleSummary";

	public static final String PORT_ROTATIONS_ID = PortRotationReportView.ID;
	public static final String PORT_ROTATIONS_SHORTNAME = "PortRotations";

	public static final String VESSEL_REPORT_ID = ConfigurableFleetReportView.ID;
	public static final String VESSEL_REPORT_SHORTNAME = "VesselReport";

	public static final String VERTICAL_REPORT_ID = "com.mmxlabs.lingo.reports.verticalreport";
	public static final String VERTICAL_REPORT_SHORTNAME = "VerticalReport";

	public static final String LATENESS_REPORT_ID = LatenessReportView.ID;
	public static final String LATENESS_REPORT_SHORTNAME = "LatenessReport";

	public static final String CAPACITY_REPORT_ID = CapacityViolationReportView.ID;
	public static final String CAPACITY_REPORT_SHORTNAME = "CapacityReport";

	public static final String COOLDOWN_REPORT_ID = CooldownReportView.ID;
	public static final String COOLDOWN_REPORT_SHORTNAME = "CooldownReport";

	public static final String HEADLINE_REPORT_ID = HeadlineReportView.ID;
	public static final String HEADLINE_REPORT_SHORTNAME = "HeadlineReport";

	public static final String KPI_REPORT_ID = KPIReportView.ID;
	public static final String KPI_REPORT_SHORTNAME = "KPIReport";

	public static final String CHANGESET_REPORT_ID = "com.mmxlabs.lingo.reports.views.changeset.ChangeSetView";
	public static final String CHANGESET_REPORT_SHORTNAME = "ChangeSetReport";

	public static final String ACTIONPLAN_REPORT_ID = "com.mmxlabs.lingo.reports.views.changeset.ActionSetView";
	public static final String ACTIONPLAN_REPORT_SHORTNAME = "ActionPlanReport";

	public static final String EXPOSURES_REPORT_ID = "com.mmxlabs.shiplingo.platform.reports.views.ExposureReportView";
	public static final String EXPOSURES_REPORT_SHORTNAME = "ExposuresReport";

	public static final String CARGO_ECONS_REPORT_ID = "com.mmxlabs.shiplingo.platform.reports.views.CargoEconsReport";
	public static final String CARGO_ECONS_REPORT_SHORTNAME = "CargoEcons";

	@FunctionalInterface
	interface IScenarioSelection {

		void updateSelection(IViewPart view, IScenarioServiceSelectionProvider provider);

	}

	@Nullable
	public IReportContents getActionPlanReportContents(final List<ScenarioResult> scenarios, final String reportID) throws InterruptedException {
		return getReportContents(reportID, (v, p) -> {

			IActionPlanHandler handler = v.getAdapter(IActionPlanHandler.class);
			if (handler == null) {
				final EPartService partService = (EPartService) v.getViewSite().getService(EPartService.class);
				final MPart part = partService.findPart(reportID);
				handler = ((IAdaptable) part.getObject()).getAdapter(IActionPlanHandler.class);
			}
			if (handler != null) {
				handler.displayActionPlan(scenarios);
			}
		});
	}

	@Nullable
	public IReportContents getReportContents(final ScenarioResult scenario, final String reportID) throws InterruptedException {
		return getReportContents(reportID, (v, p) -> {

			final IProvideEditorInputScenario scenarioInputProvider = v.getAdapter(IProvideEditorInputScenario.class);
			if (scenarioInputProvider != null) {
				scenarioInputProvider.provideScenarioInstance(scenario);
			}

			p.deselectAll(true);
			p.select(scenario, true);
		});
	}

	@Nullable
	public IReportContents getReportContents(final @NonNull ScenarioResult pinScenario, @NonNull final ScenarioResult ref, final String reportID) throws InterruptedException {
		return getReportContents(reportID, (v, p) -> {
			p.deselectAll(true);
			p.select(pinScenario, true);
			p.setPinnedInstance(pinScenario, true);
			p.select(ref, true);
		});
	}

	@Nullable
	public IReportContents getReportContents(final String reportID, final @NonNull IScenarioSelection callable) throws InterruptedException {

		// Get reference to the selection provider service

		return ServiceHelper.withCheckedService(IScenarioServiceSelectionProvider.class, provider -> {

			final IViewPart[] view = new IViewPart[1];
			final IReportContents[] contents = new IReportContents[1];

			// Step 1 open the view, release UI thread
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					Assert.assertNotNull(activePage);
					try {
						// Close the existing view reference. E.g. because we have changed the license features.
						final IViewReference ref = activePage.findViewReference(reportID);
						if (ref != null) {
							// This will dispose the view
							activePage.hideView(ref);
						}

						// Clear existing selection so newly opened view does not pick up any prior data.
						provider.deselectAll(true);

						view[0] = activePage.showView(reportID);
						Assert.assertNotNull(view[0]);
						activePage.activate(view[0]);

					} catch (final PartInitException e) {
						e.printStackTrace();
					}
				}
			});

			// Step two set the new selection, release UI thread
			Thread.sleep(1000);
			Thread.yield();

			RunnerHelper.exec(() -> {
				callable.updateSelection(view[0], provider);
			}, true);
			Thread.yield();
			Thread.sleep(1000);

			// Step 3, obtain report contents
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					contents[0] = (IReportContents) view[0].getAdapter(IReportContents.class);
					if (contents[0] == null) {
						if (view[0] instanceof E4PartWrapper) {
							final E4PartWrapper e4PartWrapper = (E4PartWrapper) view[0];
							final IViewSite viewSite = view[0].getViewSite();
							final EPartService service = (EPartService) viewSite.getService(EPartService.class);
							final MPart p = service.findPart(reportID);
							if (p != null) {
								final Object o = p.getObject();
								if (o instanceof IAdaptable) {
									final IAdaptable adaptable = (IAdaptable) o;
									contents[0] = (IReportContents) adaptable.getAdapter(IReportContents.class);
								}
							}
							// EModelService
							// EPartService
						}
					}
				}
			});

			return contents[0];
		});
	}
}