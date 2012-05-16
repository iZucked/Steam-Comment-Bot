/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.app.intro;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class EditingPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(final IPageLayout layout) {

		
//		final IFolderLayout navFolder = layout.createFolder("navFolder", IPageLayout.LEFT, 0.3f, IPageLayout.ID_EDITOR_AREA);
//		navFolder.addView("com.mmxlabs.scenario.service.ui.navigator");
//		layout.addShowViewShortcut("com.mmxlabs.scenario.service.ui.navigator");


		layout.addShowViewShortcut("org.eclipse.pde.runtime.LogView");

		layout.addPerspectiveShortcut("com.mmxlabs.shiplingo.platform.app.perspective.optimisation");
		layout.addPerspectiveShortcut("com.mmxlabs.shiplingo.platform.app.perspective.analysis");
		
		final IFolderLayout dataFolder = layout.createFolder("dataFolder", IPageLayout.RIGHT, 0.75f, IPageLayout.ID_EDITOR_AREA);
		dataFolder.addView("com.mmxlabs.models.lng.port.editor.views.PortView");
		dataFolder.addView("com.mmxlabs.models.lng.fleet.editor.views.VesselView");
		dataFolder.addView("com.mmxlabs.models.lng.fleet.editor.views.VesselClassView");
		
		layout.addShowViewShortcut("com.mmxlabs.models.lng.port.editor.views.PortView");
		layout.addShowViewShortcut("com.mmxlabs.models.lng.fleet.editor.views.VesselView");
		layout.addShowViewShortcut("com.mmxlabs.models.lng.fleet.editor.views.VesselClassView");
	}
}
