/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.intro;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewLayout;

public class EditingPerspective implements IPerspectiveFactory {

	private static final String CostsRoot = "com.mmxlabs.models.lng.pricing.editor.";

	@Override
	public void createInitialLayout(final IPageLayout layout) {

		// final IFolderLayout costsFolder = layout.createFolder("costsFolder", IPageLayout.RIGHT, 0.75f, IPageLayout.ID_EDITOR_AREA);

		layout.addView(CostsRoot + "CanalCostsView", IPageLayout.RIGHT, 0.75f, IPageLayout.ID_EDITOR_AREA);
		layout.addView(CostsRoot + "PortCostsView", IPageLayout.BOTTOM, 0.60f, CostsRoot + "CanalCostsView");
		layout.addView(CostsRoot + "CooldownCostsView", IPageLayout.BOTTOM, 0.5f, CostsRoot + "PortCostsView");

		// Scenario Navigator
		{
			final IFolderLayout scenarioArea = layout.createFolder("scenarioArea", IPageLayout.LEFT, 0.25f, IPageLayout.ID_EDITOR_AREA);

			scenarioArea.addView("com.mmxlabs.scenario.service.ui.navigator");
			final IViewLayout viewLayout = layout.getViewLayout("com.mmxlabs.scenario.service.ui.navigator");
			viewLayout.setCloseable(false);
		}

		final IFolderLayout physicalFolder = layout.createFolder("physicalFolder", IPageLayout.TOP, 0.75f, IPageLayout.ID_EDITOR_AREA);
		physicalFolder.addView("com.mmxlabs.models.lng.port.editor.views.PortView");
		physicalFolder.addView("com.mmxlabs.models.lng.fleet.editor.views.VesselAndClassView");

		final IFolderLayout miscFolder = layout.createFolder("miscFolder", IPageLayout.LEFT, 0.25f, IPageLayout.ID_EDITOR_AREA);
		miscFolder.addView("com.mmxlabs.models.ui.validation.views.ValidationProblemsView");

		layout.addShowViewShortcut("com.mmxlabs.models.lng.port.editor.views.PortView");
		layout.addShowViewShortcut("com.mmxlabs.models.lng.fleet.editor.views.VesselAndClassView");
		layout.addShowViewShortcut("com.mmxlabs.models.lng.pricing.editor.CanalCostsView");
		layout.addShowViewShortcut("com.mmxlabs.models.lng.pricing.editor.PortCostsView");
		layout.addShowViewShortcut("com.mmxlabs.models.lng.pricing.editor.CooldownCostsView");
		layout.addShowViewShortcut("com.mmxlabs.models.ui.validation.views.ValidationProblemsView");
		layout.addShowViewShortcut("org.eclipse.pde.runtime.LogView");

//		layout.addShowViewShortcut("com.mmxlabs.models.lng.spotmarkets.editor.views.DESPurchaseSpotMarketView");
//		layout.addShowViewShortcut("com.mmxlabs.models.lng.spotmarkets.editor.views.FOBSalesSpotMarketView");

		layout.addPerspectiveShortcut("com.mmxlabs.lingo.app.perspective.analysis");
	}
}
