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
import com.mmxlabs.jobcontroller.views.JobManagerView;
import com.mmxlabs.scheduleview.views.SchedulerView;

public class Perspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(final IPageLayout layout) {

		final IFolderLayout navFolder = layout.createFolder("navFolder",
				IPageLayout.LEFT, 0.2f, IPageLayout.ID_EDITOR_AREA);
		navFolder.addView(IPageLayout.ID_RES_NAV);

		layout.addView(JobManagerView.ID, IPageLayout.BOTTOM, 0.3f, "navFolder");

		final IFolderLayout propertiesFolder = layout.createFolder(
				"propertiesFolder", IPageLayout.BOTTOM, 0.2f,
				IPageLayout.ID_EDITOR_AREA);
		propertiesFolder.addView(IPageLayout.ID_PROP_SHEET);

		layout.addView(SchedulerView.ID, IPageLayout.BOTTOM, 0.3f,
				"propertiesFolder");

		final IFolderLayout reportsFolder = layout.createFolder(
				"reportsFolder", IPageLayout.BOTTOM, 0.5f, SchedulerView.ID);
		reportsFolder.addView(TotalsReportView.ID);
		reportsFolder.addView(FitnessReportView.ID);
		reportsFolder.addView(CargoReportView.ID);
		reportsFolder.addView(PortRotationReportView.ID);

		layout.addView("org.eclipse.pde.runtime.LogView", IPageLayout.BOTTOM,
				0.3f, JobManagerView.ID);

		layout.addShowViewShortcut(SchedulerView.ID);
		layout.addShowViewShortcut(JobManagerView.ID);
		layout.addShowViewShortcut(TotalsReportView.ID);
		layout.addShowViewShortcut(CargoReportView.ID);
		layout.addShowViewShortcut(FitnessReportView.ID);
		layout.addShowViewShortcut(PortRotationReportView.ID);

		layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		layout.addShowViewShortcut("org.eclipse.pde.runtime.LogView");
	}
}
