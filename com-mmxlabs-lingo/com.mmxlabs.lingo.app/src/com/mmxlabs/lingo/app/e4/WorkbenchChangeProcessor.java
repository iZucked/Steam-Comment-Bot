/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.e4;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainer;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainerElement;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindowElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.rcp.common.application.E4ModelHelper;

public class WorkbenchChangeProcessor {

	@Execute
	void process(@NonNull final MApplication application, @NonNull final EModelService modelService) {

		// Added for 3.8.x to 3.9.0 changes
		// Rename Diff Tools Perspective to Compare
		for (final MWindow window : application.getChildren()) {
			for (final MWindowElement mWindowElement : window.getChildren()) {
				if (mWindowElement instanceof MPartSashContainer) {
					final MPartSashContainer mPartSashContainer = (MPartSashContainer) mWindowElement;
					for (final MPartSashContainerElement mPartSashContainerElement : mPartSashContainer.getChildren()) {
						if (mPartSashContainerElement instanceof MPerspectiveStack) {
							final MPerspectiveStack mPerspectiveStack = (MPerspectiveStack) mPartSashContainerElement;
							for (final MPerspective perspective : mPerspectiveStack.getChildren()) {
								if (perspective.getElementId().equals("com.mmxlabs.lingo.reports.diff.DiffPerspective") && perspective.getLabel().equals("Diff Tools")) {
									perspective.setLabel("Compare");
								}
							}
						}
					}
				}
			}
		}

		E4ModelHelper.removeViewPart("com.mmxlabs.lingo.reports.diff.DiffGroupView", application, modelService);
		E4ModelHelper.removeViewPart("com.mmxlabs.shiplingo.platform.reports.views.ExposureReportView", application, modelService);
	}
}
