/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewLayout;

import com.mmxlabs.lingo.reports.views.fleet.ConfigurableFleetReportView;
import com.mmxlabs.lingo.reports.views.schedule.ConfigurableScheduleReportView;
import com.mmxlabs.lingo.reports.views.standard.HorizontalKPIReportView;

public class DiffPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(final IPageLayout layout) {
		layout.addActionSet("com.mmxlabs.lingo.reports.diff.actionset");
		layout.setEditorAreaVisible(false);

		final IFolderLayout diffArea = layout.createFolder("diffArea", IPageLayout.LEFT, 0.2f, IPageLayout.ID_EDITOR_AREA);
		final IFolderLayout ganttArea = layout.createFolder("ganttArea", IPageLayout.BOTTOM, 0.25f, IPageLayout.ID_EDITOR_AREA);
		final IFolderLayout reportsArea = layout.createFolder("reportsArea", IPageLayout.TOP, 0.65f, IPageLayout.ID_EDITOR_AREA);
		final IFolderLayout fleetReportArea = layout.createFolder("fleetReportsArea", IPageLayout.BOTTOM, 0.65f, "diffArea");

		diffArea.addView("com.mmxlabs.lingo.reports.diff.DiffGroupView");
		diffArea.addView("com.mmxlabs.scenario.service.ui.navigator");
		reportsArea.addView(ConfigurableScheduleReportView.ID);
		fleetReportArea.addView(ConfigurableFleetReportView.ID);
		
		ganttArea.addView("com.mmxlabs.scheduleview.views.SchedulerView");

		// Horizontal KPI
		layout.addStandaloneView(HorizontalKPIReportView.ID, false, IPageLayout.TOP, 0.1f, "reportsArea");
		final IViewLayout viewLayout = layout.getViewLayout(HorizontalKPIReportView.ID);
		viewLayout.setCloseable(false);
		viewLayout.setMoveable(false);

		layout.addShowViewShortcut(HorizontalKPIReportView.ID);
		layout.addShowViewShortcut(ConfigurableScheduleReportView.ID);
		layout.addShowViewShortcut(ConfigurableFleetReportView.ID);
		layout.addShowViewShortcut("com.mmxlabs.scheduleview.views.SchedulerView");
		layout.addShowViewShortcut("com.mmxlabs.lingo.reports.diff.DiffGroupView");
		layout.addShowViewShortcut("com.mmxlabs.scenario.service.ui.navigator");

		layout.addPerspectiveShortcut("com.mmxlabs.shiplingo.platform.app.perspective.editing");
		layout.addPerspectiveShortcut("com.mmxlabs.shiplingo.platform.app.perspective.analysis");

	}

}
