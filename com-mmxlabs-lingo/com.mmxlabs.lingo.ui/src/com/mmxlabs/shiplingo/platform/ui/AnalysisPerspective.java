/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.mmxlabs.shiplingo.platform.reports.views.BasicCargoReportView;
import com.mmxlabs.shiplingo.platform.reports.views.CargoPnLReportView;
import com.mmxlabs.shiplingo.platform.reports.views.CargoReportView;
import com.mmxlabs.shiplingo.platform.reports.views.CooldownReportView;
import com.mmxlabs.shiplingo.platform.reports.views.FitnessReportView;
import com.mmxlabs.shiplingo.platform.reports.views.HorizontalKPIReportView;
import com.mmxlabs.shiplingo.platform.reports.views.KPIReportView;
import com.mmxlabs.shiplingo.platform.reports.views.LatenessReportView;
import com.mmxlabs.shiplingo.platform.reports.views.NinetyDayCargoReportView;
import com.mmxlabs.shiplingo.platform.reports.views.PortRotationReportView;
import com.mmxlabs.shiplingo.platform.reports.views.SchedulePnLReport;
import com.mmxlabs.shiplingo.platform.reports.views.TotalsHierarchyView;
import com.mmxlabs.shiplingo.platform.reports.views.TotalsReportView;
import com.mmxlabs.shiplingo.platform.scheduleview.views.SchedulerView;

public class AnalysisPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(final IPageLayout layout) {

		final IFolderLayout reportsFolder = layout.createFolder("reportsFolder", IPageLayout.BOTTOM, 0.65f, IPageLayout.ID_EDITOR_AREA);
		final IFolderLayout miscFolder = layout.createFolder("miscFolder", IPageLayout.LEFT, 0.25f, "reportsFolder");
		// final IFolderLayout KPIBannerFolder = layout.createFolder("KPIBannerFolder", IPageLayout.TOP, 0.05f, "IPageLayout.ID_EDITOR_AREA");
		// final IFolderLayout cargoEconsFolder = layout.createFolder("cargoEconsFolder", IPageLayout.LEFT, 0.25f, "IPageLayout.ID_EDITOR_AREA");

		miscFolder.addView("com.mmxlabs.models.ui.validation.views.ValidationProblemsView");
		miscFolder.addView("org.eclipse.pde.runtime.LogView");
		miscFolder.addPlaceholder(TotalsHierarchyView.ID);

		layout.addPlaceholder(HorizontalKPIReportView.ID, IPageLayout.TOP, 0.05f, IPageLayout.ID_EDITOR_AREA);

		// KPIBannerFolder.addPlaceholder(HorizontalKPIReportView.ID);

		// reportsFolder.addView(KPIReportView.ID);
		reportsFolder.addView(SchedulerView.ID);
		// reportsFolder.addView(CargoPnLReportView.ID);
		reportsFolder.addView(CargoReportView.ID);
		reportsFolder.addPlaceholder(NinetyDayCargoReportView.ID);
		reportsFolder.addPlaceholder(TotalsReportView.ID);
		reportsFolder.addPlaceholder(BasicCargoReportView.ID);
		reportsFolder.addPlaceholder(SchedulePnLReport.ID);
		reportsFolder.addPlaceholder(PortRotationReportView.ID);
		reportsFolder.addPlaceholder(LatenessReportView.ID);
		reportsFolder.addPlaceholder(CooldownReportView.ID);
		reportsFolder.addPlaceholder(FitnessReportView.ID);

		// layout.addView(CargoEconsReport.ID, IPageLayout.RIGHT, 0.85f, IPageLayout.ID_EDITOR_AREA);

		layout.addShowViewShortcut(KPIReportView.ID);
		layout.addShowViewShortcut(CargoReportView.ID);
		layout.addShowViewShortcut(NinetyDayCargoReportView.ID);
		layout.addShowViewShortcut(CargoPnLReportView.ID);
		layout.addShowViewShortcut(SchedulePnLReport.ID);
		layout.addShowViewShortcut(SchedulerView.ID);
		layout.addShowViewShortcut(TotalsReportView.ID);
		layout.addShowViewShortcut(TotalsHierarchyView.ID);
		layout.addShowViewShortcut(BasicCargoReportView.ID);
		layout.addShowViewShortcut(PortRotationReportView.ID);
		layout.addShowViewShortcut(LatenessReportView.ID);
		layout.addShowViewShortcut(CooldownReportView.ID);
		layout.addShowViewShortcut(FitnessReportView.ID);

		layout.addShowViewShortcut("org.eclipse.pde.runtime.LogView");

		layout.addPerspectiveShortcut("com.mmxlabs.shiplingo.platform.app.perspective.editing");
		layout.addPerspectiveShortcut("com.mmxlabs.shiplingo.platform.app.perspective.analysis");
	}
}
