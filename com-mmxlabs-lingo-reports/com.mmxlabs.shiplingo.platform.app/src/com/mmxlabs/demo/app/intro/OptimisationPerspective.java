/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.demo.app.intro;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.mmxlabs.demo.reports.views.BasicCargoReportView;
import com.mmxlabs.demo.reports.views.CargoReportView;
import com.mmxlabs.demo.reports.views.CooldownReportView;
import com.mmxlabs.demo.reports.views.FitnessReportView;
import com.mmxlabs.demo.reports.views.LatenessReportView;
import com.mmxlabs.demo.reports.views.PortRotationReportView;
import com.mmxlabs.demo.reports.views.TotalsHierarchyView;
import com.mmxlabs.demo.reports.views.TotalsReportView;
import com.mmxlabs.scheduleview.views.SchedulerView;

public class OptimisationPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(final IPageLayout layout) {

		layout.setEditorAreaVisible(false);

		final IFolderLayout navFolder = layout.createFolder("navFolder", IPageLayout.LEFT, 0.2f, IPageLayout.ID_EDITOR_AREA);
		navFolder.addView("com.mmxlabs.rcp.navigator");

		layout.addView(SchedulerView.ID, IPageLayout.BOTTOM, 0.3f, IPageLayout.ID_EDITOR_AREA);

		final IFolderLayout reportsFolder = layout.createFolder("reportsFolder", IPageLayout.BOTTOM, 0.5f, SchedulerView.ID);

		reportsFolder.addView(TotalsReportView.ID);
		reportsFolder.addView(TotalsHierarchyView.ID);
		reportsFolder.addView(BasicCargoReportView.ID);
		reportsFolder.addView(CargoReportView.ID);
		reportsFolder.addView(LatenessReportView.ID);
		reportsFolder.addView(CooldownReportView.ID);
		reportsFolder.addView(FitnessReportView.ID);

		layout.addShowViewShortcut(SchedulerView.ID);
		layout.addShowViewShortcut(TotalsReportView.ID);
		layout.addShowViewShortcut(TotalsHierarchyView.ID);
		layout.addShowViewShortcut(CargoReportView.ID);
		layout.addShowViewShortcut(BasicCargoReportView.ID);
		layout.addShowViewShortcut(FitnessReportView.ID);
		layout.addShowViewShortcut(LatenessReportView.ID);
		layout.addShowViewShortcut(CooldownReportView.ID);
		layout.addShowViewShortcut(PortRotationReportView.ID);

		layout.addShowViewShortcut("com.mmxlabs.rcp.navigator");
		layout.addShowViewShortcut("org.eclipse.pde.runtime.LogView");

		layout.addPerspectiveShortcut("com.mmxlabs.demo.app.perspective.editing");
	}
}
