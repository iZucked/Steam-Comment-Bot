/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
		final IFolderLayout editorsAreaTop = layout.createFolder("editorsArea", IPageLayout.TOP, 0.4f, IPageLayout.ID_EDITOR_AREA);
		final IFolderLayout editorsAreaBottom = layout.createFolder("editorsArea", IPageLayout.BOTTOM, 0.4f, IPageLayout.ID_EDITOR_AREA);

		browserArea.addView("com.mmxlabs.scenario.service.ui.navigator");

		editorsAreaTop.addView("com.mmxlabs.models.lng.cargo.editor.risk.DealSetTableEditorView");
		editorsAreaBottom.addView("com.mmxlabs.shiplingo.platform.reports.views.ExposureReportView");

		layout.addShowViewShortcut("com.mmxlabs.models.lng.cargo.editor.risk.DealSetTableEditorView");
		layout.addShowViewShortcut("com.mmxlabs.shiplingo.platform.reports.views.ExposureReportView");
		layout.addShowViewShortcut("com.mmxlabs.shiplingo.platform.reports.views.ExposureDetailReportView");
		
		layout.addPerspectiveShortcut("com.mmxlabs.lingo.app.perspective.editing");
		layout.addPerspectiveShortcut("com.mmxlabs.lingo.app.perspective.analysis");
	}

}
