/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class CargoModelEditorContribution extends BaseJointModelEditorContribution<CargoModel> {
	private int pageNumber = 0;
	private CargoModelViewer viewerPane;

	@Override
	public void addPages(final Composite parent) {
		this.viewerPane = new CargoModelViewer(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		viewerPane.createControl(parent);
		viewerPane.init(Collections.singletonList(CargoPackage.eINSTANCE.getCargoModel_Cargoes()), editorPart.getAdapterFactory());
		viewerPane.getViewer().setInput(modelObject);
		pageNumber = editorPart.addPage(viewerPane.getControl());
		editorPart.setPageText(pageNumber, "Cargoes");
	}

	@Override
	public void setLocked(final boolean locked) {
		viewerPane.setLocked(locked);
	}

	@Override
	public boolean canHandle(final IStatus status) {

		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			if (dcsd.getTarget() instanceof Cargo) {
				return true;
			} else if (dcsd.getTarget() instanceof LoadSlot) {
				return true;
			} else if (dcsd.getTarget() instanceof DischargeSlot) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void handle(final IStatus status) {
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
			editorPart.setActivePage(pageNumber);
			if (cargo != null) {
				viewerPane.getScenarioViewer().setSelection(new StructuredSelection(cargo), true);
			}
		}
	}
}
