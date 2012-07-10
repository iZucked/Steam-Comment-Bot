/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.editor.views;

import java.util.Arrays;

import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.ui.editorpart.VesselViewerPane_View;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;

public class VesselView extends ScenarioTableViewerView<VesselViewerPane_View> {
	@Override
	protected VesselViewerPane_View createViewerPane() {
		return new VesselViewerPane_View(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(VesselViewerPane_View pane) {
		pane.init(Arrays
				.asList(new EReference[] { FleetPackage.eINSTANCE
						.getFleetModel_Vessels() }), null);
		pane.getViewer().setInput(
				getRootObject().getSubModel(FleetModel.class));
	}
}
