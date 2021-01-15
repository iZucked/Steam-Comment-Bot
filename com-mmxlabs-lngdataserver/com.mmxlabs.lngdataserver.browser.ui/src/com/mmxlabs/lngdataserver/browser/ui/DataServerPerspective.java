/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.browser.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class DataServerPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(final IPageLayout layout) {
		layout.setEditorAreaVisible(false);

		final IFolderLayout browserArea = layout.createFolder("browserArea", IPageLayout.LEFT, 0.2f, IPageLayout.ID_EDITOR_AREA);
		final IFolderLayout editorsArea = layout.createFolder("editorsArea", IPageLayout.RIGHT, 0.8f, IPageLayout.ID_EDITOR_AREA);

		browserArea.addView(DataBrowser.ID);

//		editorsArea.addView("com.mmxlabs.lngdataserver.integration.ui.WebNavigatorView");

		layout.addShowViewShortcut(DataBrowser.ID);
//		layout.addShowViewShortcut("com.mmxlabs.lngdataserver.integration.ui.WebNavigatorView");

		layout.addPerspectiveShortcut("com.mmxlabs.lingo.app.perspective.editing");
		layout.addPerspectiveShortcut("com.mmxlabs.lingo.app.perspective.analysis");

	}

}
