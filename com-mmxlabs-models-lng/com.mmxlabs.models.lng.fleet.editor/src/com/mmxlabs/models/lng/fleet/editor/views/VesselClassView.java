package com.mmxlabs.models.lng.fleet.editor.views;

import java.util.Arrays;

import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.ui.editorpart.VesselClassViewerPane;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;

public class VesselClassView extends ScenarioTableViewerView<VesselClassViewerPane> {
	@Override
	protected VesselClassViewerPane createViewerPane() {
		return new VesselClassViewerPane(getSite().getPage(), this, this);
	}

	@Override
	protected void initViewerPane(final VesselClassViewerPane pane) {
		pane.init(Arrays
				.asList(new EReference[] { FleetPackage.eINSTANCE
						.getFleetModel_VesselClasses() }), null);
		pane.getViewer().setInput(
				getRootObject().getSubModel(FleetModel.class));
	}
}
