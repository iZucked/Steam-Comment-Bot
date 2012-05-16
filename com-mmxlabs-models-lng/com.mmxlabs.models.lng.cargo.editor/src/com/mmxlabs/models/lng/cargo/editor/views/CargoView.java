package com.mmxlabs.models.lng.cargo.editor.views;

import java.util.Arrays;

import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelViewer;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;

public class CargoView extends ScenarioTableViewerView<CargoModelViewer> {
	public static final String ID = "com.mmxlabs.models.lng.cargo.editor.CargoView";

	@Override
	protected CargoModelViewer createViewerPane() {
		return new CargoModelViewer(getSite().getPage(), this, this);
	}

	@Override
	protected void initViewerPane(CargoModelViewer pane) {
		pane.init(Arrays
				.asList(new EReference[] { CargoPackage.eINSTANCE
						.getCargoModel_Cargoes() }), null);
		pane.getViewer().setInput(
				getRootObject().getSubModel(CargoModel.class));
	}
}
