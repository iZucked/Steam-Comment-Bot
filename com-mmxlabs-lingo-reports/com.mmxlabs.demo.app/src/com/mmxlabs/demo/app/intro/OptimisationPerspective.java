/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */

package com.mmxlabs.demo.app.intro;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.mmxlabs.demo.reports.views.CargoReportView;
import com.mmxlabs.demo.reports.views.FitnessReportView;
import com.mmxlabs.demo.reports.views.PortRotationReportView;
import com.mmxlabs.demo.reports.views.TotalsReportView;
import com.mmxlabs.scheduleview.views.SchedulerView;

public class OptimisationPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(final IPageLayout layout) {

		layout.setEditorAreaVisible(false);

		final IFolderLayout navFolder = layout.createFolder("navFolder",
				IPageLayout.LEFT, 0.2f, IPageLayout.ID_EDITOR_AREA);
		navFolder.addView("com.mmxlabs.rcp.navigator");
		navFolder.addView(IPageLayout.ID_RES_NAV);

		// layout.addView(JobManagerView.ID, IPageLayout.BOTTOM, 0.3f,
		// "navFolder");

		// final IFolderLayout propertiesFolder = layout.createFolder(
		// "propertiesFolder", IPageLayout.BOTTOM, 0.2f,
		// IPageLayout.ID_EDITOR_AREA);

		layout.addView(SchedulerView.ID, IPageLayout.BOTTOM, 0.3f,
				IPageLayout.ID_EDITOR_AREA);

		final IFolderLayout reportsFolder = layout.createFolder(
				"reportsFolder", IPageLayout.BOTTOM, 0.5f, SchedulerView.ID);

		reportsFolder.addView(TotalsReportView.ID);
		reportsFolder.addView(FitnessReportView.ID);
		reportsFolder.addView(CargoReportView.ID);
		reportsFolder.addView(PortRotationReportView.ID);
		reportsFolder.addView("org.eclipse.pde.runtime.LogView");

		layout.addView(IPageLayout.ID_PROP_SHEET, IPageLayout.BOTTOM, 0.3f,
				"navFolder");

		layout.addShowViewShortcut(SchedulerView.ID);
		layout.addShowViewShortcut(TotalsReportView.ID);
		layout.addShowViewShortcut(CargoReportView.ID);
		layout.addShowViewShortcut(FitnessReportView.ID);
		layout.addShowViewShortcut(PortRotationReportView.ID);

		layout.addShowViewShortcut("com.mmxlabs.rcp.navigator");
		layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		layout.addShowViewShortcut("org.eclipse.pde.runtime.LogView");

		layout.addPerspectiveShortcut("com.mmxlabs.demo.app.perspective.editing");
	}
}
