/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */

package com.mmxlabs.demo.app.intro;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.mmxlabs.jobcontroller.views.JobManagerView;

public class EditingPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(final IPageLayout layout) {

		final IFolderLayout navFolder = layout.createFolder("navFolder",
				IPageLayout.LEFT, 0.2f, IPageLayout.ID_EDITOR_AREA);
		navFolder.addView("com.mmxlabs.rcp.navigator");
		navFolder.addView(IPageLayout.ID_RES_NAV);

		final IFolderLayout propertiesFolder = layout.createFolder(
				"propertiesFolder", IPageLayout.BOTTOM, 0.7f,
				IPageLayout.ID_EDITOR_AREA);
		propertiesFolder.addView(IPageLayout.ID_PROP_SHEET);

		propertiesFolder.addView("org.eclipse.pde.runtime.LogView");

		layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		layout.addShowViewShortcut("com.mmxlabs.rcp.navigator");
		layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
		layout.addShowViewShortcut("org.eclipse.pde.runtime.LogView");
		
		layout.addPerspectiveShortcut("com.mmxlabs.demo.app.perspective.optimisation");
	}
}
