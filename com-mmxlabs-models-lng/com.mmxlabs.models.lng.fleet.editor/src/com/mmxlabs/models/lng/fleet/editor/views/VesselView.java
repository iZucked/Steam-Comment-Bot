package com.mmxlabs.models.lng.fleet.editor.views;

import java.util.Arrays;

import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.ui.editorpart.VesselViewerPane;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;

public class VesselView extends ScenarioTableViewerView<VesselViewerPane> {
	@Override
	protected VesselViewerPane createViewerPane() {
		return new VesselViewerPane(getSite().getPage(), this, this);
	}

	@Override
	protected void initViewerPane(VesselViewerPane pane) {
		pane.init(Arrays
				.asList(new EReference[] { FleetPackage.eINSTANCE
						.getFleetModel_Vessels() }), null);
		pane.getViewer().setInput(
				getRootObject().getSubModel(FleetModel.class));
	}
}
