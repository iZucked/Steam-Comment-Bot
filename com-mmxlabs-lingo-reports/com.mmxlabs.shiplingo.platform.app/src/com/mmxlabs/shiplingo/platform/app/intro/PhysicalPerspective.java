package com.mmxlabs.shiplingo.platform.app.intro;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class PhysicalPerspective implements IPerspectiveFactory {
	@Override
	public void createInitialLayout(final IPageLayout layout) {
		final IFolderLayout navigator = layout.createFolder("navFolder", IPageLayout.LEFT, 0.2f, IPageLayout.ID_EDITOR_AREA);
		navigator.addView("com.mmxlabs.scenario.service.ui.navigator");
		
		final IFolderLayout viewsFolder = layout.createFolder("viewsFolder", IPageLayout.TOP, 1, IPageLayout.ID_EDITOR_AREA);
		
		viewsFolder.addView("com.mmxlabs.models.lng.port.editor.views.PortView");
		viewsFolder.addView("com.mmxlabs.models.lng.port.editor.views.PortGroupView");
		viewsFolder.addView("com.mmxlabs.models.lng.fleet.editor.views.VesselView");
		viewsFolder.addView("com.mmxlabs.models.lng.fleet.editor.views.VesselClassView");
		// consider other views which could go here?
		
		layout.setEditorAreaVisible(false);
	}
}