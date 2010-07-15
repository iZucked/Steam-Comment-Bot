package com.mmxlabs.demo.app.intro;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.mmxlabs.jobcontroller.views.JobManagerView;
import com.mmxlabs.scheduleview.views.SchedulerView;

public class Perspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		
		layout.addView(IPageLayout.ID_RES_NAV, IPageLayout.LEFT, 0.3f, IPageLayout.ID_EDITOR_AREA);
		layout.addView(SchedulerView.ID, IPageLayout.RIGHT,0.7f, IPageLayout.ID_EDITOR_AREA);
		
		IFolderLayout folder1 = layout.createFolder("folder1", IPageLayout.BOTTOM, 0.7f, IPageLayout.ID_EDITOR_AREA);
		
		folder1.addView(JobManagerView.ID);
		folder1.addView("org.eclipse.pde.runtime.LogView");
	}
}
