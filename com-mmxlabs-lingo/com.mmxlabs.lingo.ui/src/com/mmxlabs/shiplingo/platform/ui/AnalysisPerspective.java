/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewLayout;

import com.mmxlabs.lingo.reports.scheduleview.views.SchedulerView;
import com.mmxlabs.lingo.reports.views.portrotation.PortRotationReportView;
import com.mmxlabs.lingo.reports.views.schedule.ConfigurableScheduleReportView;
import com.mmxlabs.lingo.reports.views.standard.CapacityViolationReportView;
import com.mmxlabs.lingo.reports.views.standard.CooldownReportView;
import com.mmxlabs.lingo.reports.views.standard.FitnessReportView;
import com.mmxlabs.lingo.reports.views.standard.HeadlineReportView;
import com.mmxlabs.lingo.reports.views.standard.KPIReportView;
import com.mmxlabs.lingo.reports.views.standard.LatenessReportView;
import com.mmxlabs.lingo.reports.views.standard.TotalsHierarchyView;
import com.mmxlabs.lingo.reports.views.standard.TotalsReportView;
import com.mmxlabs.lingo.reports.views.vertical.AbstractVerticalCalendarReportView;

public class AnalysisPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(final IPageLayout layout) {

		final IFolderLayout reportsFolder = layout.createFolder("reportsFolder", IPageLayout.BOTTOM, 0.65f, IPageLayout.ID_EDITOR_AREA);
		final IFolderLayout miscFolder = layout.createFolder("miscFolder", IPageLayout.LEFT, 0.25f, "reportsFolder");

		miscFolder.addView("com.mmxlabs.models.ui.validation.views.ValidationProblemsView");
		miscFolder.addView("org.eclipse.pde.runtime.LogView");
		miscFolder.addPlaceholder(TotalsHierarchyView.ID);

		// Scenario Navigator
		{
			final IFolderLayout scenarioArea = layout.createFolder("scenarioArea", IPageLayout.LEFT, 0.25f, IPageLayout.ID_EDITOR_AREA);

			scenarioArea.addView("com.mmxlabs.scenario.service.ui.navigator");
			final IViewLayout viewLayout = layout.getViewLayout("com.mmxlabs.scenario.service.ui.navigator");
			viewLayout.setCloseable(false);
		}
		// Horizontal KPI
		{
			layout.addStandaloneView(HeadlineReportView.ID, false, IPageLayout.TOP, 0.05f, IPageLayout.ID_EDITOR_AREA);
			final IViewLayout viewLayout = layout.getViewLayout(HeadlineReportView.ID);
			viewLayout.setCloseable(false);
			viewLayout.setMoveable(false);
		}

		// Cargo Econs
		final IFolderLayout econsArea = layout.createFolder("econsArea", IPageLayout.RIGHT, 0.85f, IPageLayout.ID_EDITOR_AREA);
		econsArea.addView("com.mmxlabs.shiplingo.platform.reports.views.CargoEconsReport");
		{
			final IViewLayout viewLayout = layout.getViewLayout("com.mmxlabs.shiplingo.platform.reports.views.CargoEconsReport");
			viewLayout.setCloseable(false);
			viewLayout.setMoveable(false);
		}
		// PNL Details

		econsArea.addView("com.mmxlabs.shiplingo.platform.reports.views.PNLDetailsReport");
		{
			final IViewLayout viewLayout = layout.getViewLayout("com.mmxlabs.shiplingo.platform.reports.views.PNLDetailsReport");
			viewLayout.setCloseable(false);
			viewLayout.setMoveable(false);
		}

		reportsFolder.addView(SchedulerView.ID);
		reportsFolder.addView(AbstractVerticalCalendarReportView.ID);
		reportsFolder.addPlaceholder(TotalsReportView.ID);
		reportsFolder.addPlaceholder(ConfigurableScheduleReportView.ID);
		reportsFolder.addPlaceholder(PortRotationReportView.ID);
		reportsFolder.addPlaceholder(LatenessReportView.ID);
		reportsFolder.addPlaceholder(CooldownReportView.ID);
		reportsFolder.addPlaceholder(FitnessReportView.ID);
		reportsFolder.addPlaceholder(CapacityViolationReportView.ID);

		layout.addShowViewShortcut("com.mmxlabs.scenario.service.ui.navigator");
		layout.addShowViewShortcut("com.mmxlabs.shiplingo.platform.reports.views.PNLDetailsReport");
		layout.addShowViewShortcut("com.mmxlabs.shiplingo.platform.reports.views.CargoEconsReport");
		layout.addShowViewShortcut(KPIReportView.ID);
		layout.addShowViewShortcut(ConfigurableScheduleReportView.ID);
		layout.addShowViewShortcut(SchedulerView.ID);
		layout.addShowViewShortcut(AbstractVerticalCalendarReportView.ID);
		layout.addShowViewShortcut(TotalsReportView.ID);
		layout.addShowViewShortcut(TotalsHierarchyView.ID);
		layout.addShowViewShortcut(PortRotationReportView.ID);
		layout.addShowViewShortcut(LatenessReportView.ID);
		layout.addShowViewShortcut(CooldownReportView.ID);
		layout.addShowViewShortcut(FitnessReportView.ID);
		layout.addShowViewShortcut(CapacityViolationReportView.ID);

		layout.addShowViewShortcut("org.eclipse.pde.runtime.LogView");

		layout.addPerspectiveShortcut("com.mmxlabs.lingo.app.perspective.editing");
		layout.addPerspectiveShortcut("com.mmxlabs.lingo.app.perspective.analysis");
	}
}
