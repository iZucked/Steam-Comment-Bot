/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.editor.views;

import java.util.Arrays;

import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.ui.editorpart.VesselEventViewerPane;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;

public class VesselEventView extends ScenarioTableViewerView<VesselEventViewerPane> {

	@Override
	protected VesselEventViewerPane createViewerPane() {
		return new VesselEventViewerPane(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(VesselEventViewerPane pane) {
		pane.init(Arrays
				.asList(new EReference[] { FleetPackage.eINSTANCE
						.getFleetModel_VesselEvents() }), null);
		pane.getViewer().setInput(
				getRootObject().getSubModel(FleetModel.class));
	}
}
