/**
 * Copyright (C) Minimaxlabs, 2010
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
	public void createInitialLayout(IPageLayout layout) {
		
		
		IFolderLayout leftFolder = layout.createFolder("leftFolder", IPageLayout.LEFT, 0.2f, IPageLayout.ID_EDITOR_AREA);
		leftFolder.addView(IPageLayout.ID_RES_NAV);
		
		IFolderLayout leftBottomFolder = layout.createFolder("leftBottomFolder", IPageLayout.BOTTOM, 0.5f, "leftFolder");
		leftBottomFolder.addView(TotalsReportView.ID);
		leftBottomFolder.addView(FitnessReportView.ID);
		leftBottomFolder.addView(CargoReportView.ID);
		leftBottomFolder.addView(PortRotationReportView.ID);
		
		
		
		
		layout.addView(SchedulerView.ID, IPageLayout.RIGHT,0.3f, IPageLayout.ID_EDITOR_AREA);
		
		
		
		IFolderLayout folder1 = layout.createFolder("folder1", IPageLayout.BOTTOM, 0.7f, IPageLayout.ID_EDITOR_AREA);
		
		folder1.addView(JobManagerView.ID);
		folder1.addView("org.eclipse.pde.runtime.LogView");
		
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
