/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
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
			}
		}

		return false;
	}

	@Override
	public void handle(final IStatus status) {
		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			if (dcsd.getTarget() instanceof Cargo) {
				final Cargo cargo = (Cargo) dcsd.getTarget();
				editorPart.setActivePage(pageNumber);
				viewerPane.getScenarioViewer().setSelection(new StructuredSelection(cargo), true);
			}
		}
	}
}
