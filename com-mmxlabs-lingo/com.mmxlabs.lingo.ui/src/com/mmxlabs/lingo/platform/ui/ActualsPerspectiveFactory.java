/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.platform.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class ActualsPerspectiveFactory implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		// folder to add scenario navigator through PerspectiveExtn
		final IFolderLayout navigatorFolder = layout.createFolder("navigatorFolder", IPageLayout.LEFT, 0.2f, IPageLayout.ID_EDITOR_AREA);
		layout.addView("com.mmxlabs.models.lng.actuals.editor.ActualsEditorView", IPageLayout.TOP, 0.45f, IPageLayout.ID_EDITOR_AREA);
		navigatorFolder.addView("com.mmxlabs.models.ui.validation.views.ValidationProblemsView");
	}

}
