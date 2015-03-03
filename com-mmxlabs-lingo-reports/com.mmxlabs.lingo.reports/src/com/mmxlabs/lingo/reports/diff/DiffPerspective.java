/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.mmxlabs.lingo.reports.views.schedule.ConfigurableScheduleReportView;
import com.mmxlabs.lingo.reports.views.standard.HorizontalKPIReportView;

public class DiffPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(final IPageLayout layout) {
		layout.addActionSet("com.mmxlabs.lingo.reports.diff.actionset");
		layout.setEditorAreaVisible(false);
//		layout.setFixed();
		
		final IFolderLayout diffArea = layout.createFolder("diffArea", IPageLayout.LEFT, 0.32f, IPageLayout.ID_EDITOR_AREA);
		final IFolderLayout ganttArea = layout.createFolder("ganttArea", IPageLayout.BOTTOM, 0.25f, IPageLayout.ID_EDITOR_AREA);
		final IFolderLayout reportsArea = layout.createFolder("reportsArea", IPageLayout.BOTTOM, 0.65f, IPageLayout.ID_EDITOR_AREA);
//		final IFolderLayout miscFolder = layout.createFolder("miscFolder", IPageLayout.LEFT, 0.25f, "reportsFolder");
		// final IFolderLayout KPIBannerFolder = layout.createFolder("KPIBannerFolder", IPageLayout.TOP, 0.05f, "IPageLayout.ID_EDITOR_AREA");
		// final IFolderLayout cargoEconsFolder = layout.createFolder("cargoEconsFolder", IPageLayout.LEFT, 0.25f, "IPageLayout.ID_EDITOR_AREA");

		diffArea.addView("com.mmxlabs.lingo.reports.diff.DiffGroupView");
		diffArea.addView("com.mmxlabs.scenario.service.ui.navigator");
		reportsArea.addView(ConfigurableScheduleReportView.ID);
//		miscFolder.addPlaceholder(TotalsHierarchyView.ID);

		layout.addPlaceholder(HorizontalKPIReportView.ID, IPageLayout.TOP, 0.05f, IPageLayout.ID_EDITOR_AREA);

//		 KPIBannerFolder.addPlaceholder(HorizontalKPIReportView.ID);

		// reportsFolder.addView(KPIReportView.ID);
		ganttArea.addView("com.mmxlabs.scheduleview.views.SchedulerView");
//		reportsFolder.addPlaceholder(TotalsReportView.ID);
//		reportsFolder.addPlaceholder(ConfigurableScheduleReportView.ID);
//		reportsFolder.addPlaceholder(PortRotationReportView.ID);
//		reportsFolder.addPlaceholder(LatenessReportView.ID);
//		reportsFolder.addPlaceholder(CooldownReportView.ID);
//		reportsFolder.addPlaceholder(FitnessReportView.ID);
//		reportsFolder.addPlaceholder(CapacityViolationReportView.ID);

		// layout.addView(CargoEconsReport.ID, IPageLayout.RIGHT, 0.85f, IPageLayout.ID_EDITOR_AREA);

//		layout.addShowViewShortcut(KPIReportView.ID);
//		layout.addShowViewShortcut(ConfigurableScheduleReportView.ID);
//		layout.addShowViewShortcut(SchedulerView.ID);
//		layout.addShowViewShortcut(TotalsReportView.ID);
//		layout.addShowViewShortcut(TotalsHierarchyView.ID);
//		layout.addShowViewShortcut(PortRotationReportView.ID);
//		layout.addShowViewShortcut(LatenessReportView.ID);
//		layout.addShowViewShortcut(CooldownReportView.ID);
//		layout.addShowViewShortcut(FitnessReportView.ID);
//		layout.addShowViewShortcut(CapacityViolationReportView.ID);
//
//		layout.addShowViewShortcut("org.eclipse.pde.runtime.LogView");

		layout.addPerspectiveShortcut("com.mmxlabs.shiplingo.platform.app.perspective.editing");
		layout.addPerspectiveShortcut("com.mmxlabs.shiplingo.platform.app.perspective.analysis");
	}

}
