/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.internal.e4.compatibility.ModeledPageLayout;

public class RiskModelPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(final IPageLayout layout) {

		final IFolderLayout browserArea = layout.createFolder("browserArea", IPageLayout.LEFT, 0.2f, IPageLayout.ID_EDITOR_AREA);
		final IFolderLayout editorsAreaBottom = layout.createFolder("editorsArea", IPageLayout.BOTTOM, 0.6f, IPageLayout.ID_EDITOR_AREA);

		browserArea.addView("com.mmxlabs.scenario.service.ui.navigator");

		 if (layout instanceof ModeledPageLayout) {
			 final ModeledPageLayout modeledPageLayout = (ModeledPageLayout) layout;
			 modeledPageLayout.stackView("com.mmxlabs.models.lng.cargo.editor.risk.DealSetTableEditorView", IPageLayout.ID_EDITOR_AREA, true);
			 modeledPageLayout.stackView("com.mmxlabs.models.lng.cargo.editor.risk.CargoSelectionTableEditorView", IPageLayout.ID_EDITOR_AREA, true);
		 }
		
		editorsAreaBottom.addView("com.mmxlabs.shiplingo.platform.reports.views.ExposureReportView");

		layout.addShowViewShortcut("com.mmxlabs.models.lng.cargo.editor.risk.DealSetTableEditorView");
		layout.addShowViewShortcut("com.mmxlabs.models.lng.cargo.editor.risk.CargoSelectionEditorView");
		layout.addShowViewShortcut("com.mmxlabs.shiplingo.platform.reports.views.ExposureReportView");
		layout.addShowViewShortcut("com.mmxlabs.shiplingo.platform.reports.views.ExposureDetailReportView");
		
		layout.addPerspectiveShortcut("com.mmxlabs.lingo.app.perspective.editing");
		layout.addPerspectiveShortcut("com.mmxlabs.lingo.app.perspective.analysis");
	}

}
