/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.platform.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class RiskModelPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(final IPageLayout layout) {
		layout.setEditorAreaVisible(false);

		final IFolderLayout browserArea = layout.createFolder("browserArea", IPageLayout.LEFT, 0.2f, IPageLayout.ID_EDITOR_AREA);
		final IFolderLayout editorsArea = layout.createFolder("editorsArea", IPageLayout.RIGHT, 0.8f, IPageLayout.ID_EDITOR_AREA);

		browserArea.addView("com.mmxlabs.scenario.service.ui.navigator");

		editorsArea.addView("com.mmxlabs.models.lng.cargo.editor.risk.DealSetTableEditorView");

		layout.addShowViewShortcut("com.mmxlabs.models.lng.cargo.editor.risk.DealSetTableEditorView");
		layout.addPerspectiveShortcut("com.mmxlabs.lingo.app.perspective.editing");
		layout.addPerspectiveShortcut("com.mmxlabs.lingo.app.perspective.analysis");

	}

}
