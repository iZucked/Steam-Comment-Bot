/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewLayout;

import com.mmxlabs.lingo.reports.views.fleet.ConfigurableFleetReportView;
import com.mmxlabs.lingo.reports.views.schedule.ConfigurableScheduleReportView;
import com.mmxlabs.lingo.reports.views.standard.CargoEconsReport;
import com.mmxlabs.lingo.reports.views.standard.HeadlineReportView;

public class DiffPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(final IPageLayout layout) {
		layout.addActionSet("com.mmxlabs.lingo.reports.diff.actionset");
		layout.setEditorAreaVisible(false);

		final IFolderLayout ganttArea = layout.createFolder("ganttArea", IPageLayout.BOTTOM, 0.5f, IPageLayout.ID_EDITOR_AREA);
		final IFolderLayout diffArea = layout.createFolder("diffArea", IPageLayout.LEFT, 0.2f, IPageLayout.ID_EDITOR_AREA);

		// Horizontal KPI
		layout.addStandaloneView(HeadlineReportView.ID, false, IPageLayout.TOP, 0.06f, IPageLayout.ID_EDITOR_AREA);
		final IViewLayout viewLayout = layout.getViewLayout(HeadlineReportView.ID);
		viewLayout.setCloseable(false);
		viewLayout.setMoveable(false);

		final IFolderLayout changeSetArea = layout.createFolder("reportsArea", IPageLayout.TOP, 0.80f, IPageLayout.ID_EDITOR_AREA);
		final IFolderLayout fleetReportArea = layout.createFolder("fleetReportsArea", IPageLayout.LEFT, 0.2f, "ganttArea");
		final IFolderLayout econsArea = layout.createFolder("econsArea", IPageLayout.RIGHT, 0.85f, "reportsArea");

		diffArea.addView("com.mmxlabs.scenario.service.ui.navigator");
		changeSetArea.addView("com.mmxlabs.lingo.reports.views.changeset.ChangeSetView");
		changeSetArea.addPlaceholder("com.mmxlabs.lingo.reports.views.changeset.ActionSetView");
		fleetReportArea.addView(ConfigurableFleetReportView.ID);

		ganttArea.addView(ConfigurableScheduleReportView.ID);
		ganttArea.addView("com.mmxlabs.scheduleview.views.SchedulerView");

		econsArea.addView(CargoEconsReport.ID);
		econsArea.addView("com.mmxlabs.shiplingo.platform.reports.views.PNLDetailsReport");

		layout.addShowViewShortcut(CargoEconsReport.ID);
		layout.addShowViewShortcut("com.mmxlabs.shiplingo.platform.reports.views.PNLDetailsReport");

		layout.addShowViewShortcut(HeadlineReportView.ID);
		layout.addShowViewShortcut(ConfigurableScheduleReportView.ID);
		layout.addShowViewShortcut(ConfigurableFleetReportView.ID);
		layout.addShowViewShortcut("com.mmxlabs.scheduleview.views.SchedulerView");
		layout.addShowViewShortcut("com.mmxlabs.lingo.reports.diff.DiffGroupView");
		layout.addShowViewShortcut("com.mmxlabs.scenario.service.ui.navigator");

		layout.addShowViewShortcut("com.mmxlabs.lingo.reports.views.changeset.ChangeSetView");
		layout.addShowViewShortcut("com.mmxlabs.lingo.reports.views.changeset.ActionSetView");

		layout.addPerspectiveShortcut("com.mmxlabs.lingo.app.perspective.editing");
		layout.addPerspectiveShortcut("com.mmxlabs.lingo.app.perspective.analysis");

	}

}
