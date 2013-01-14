/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.views;

import java.util.Arrays;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.StructuredSelection;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelViewer;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class CargoView extends ScenarioTableViewerView<CargoModelViewer> {
	public static final String ID = "com.mmxlabs.models.lng.cargo.editor.CargoView";

	@Override
	protected CargoModelViewer createViewerPane() {
		return new CargoModelViewer(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(final CargoModelViewer pane) {
		pane.init(Arrays.asList(new EReference[] { CargoPackage.eINSTANCE.getCargoModel_Cargoes() }), null);
		pane.getViewer().setInput(getRootObject().getSubModel(CargoModel.class));
	}

	@Override
	public void openStatus(final IStatus status) {
		if (status.isMultiStatus()) {
			// Try first element
			openStatus(status.getChildren()[0]);
		}

		if (status instanceof DetailConstraintStatusDecorator) {

			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			Cargo cargo = null;
			if (dcsd.getTarget() instanceof Cargo) {
				cargo = (Cargo) dcsd.getTarget();
			} else if (dcsd.getTarget() instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) dcsd.getTarget();
				cargo = loadSlot.getCargo();
			} else if (dcsd.getTarget() instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) dcsd.getTarget();
				cargo = dischargeSlot.getCargo();
			}
			if (cargo != null) {
				getSite().getPage().activate(this);
				setSelection(new StructuredSelection(cargo), true);
			}
		}
	}
}
