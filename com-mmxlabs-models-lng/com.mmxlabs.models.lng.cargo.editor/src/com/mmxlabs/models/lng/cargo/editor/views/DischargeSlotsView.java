/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.views;

import java.util.Arrays;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.StructuredSelection;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.DischargeSlotViewer;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class DischargeSlotsView extends ScenarioTableViewerView<DischargeSlotViewer> {
	public static final String ID = "com.mmxlabs.models.lng.cargo.editor.DischargeSlotsView";

	@Override
	protected DischargeSlotViewer createViewerPane() {
		return new DischargeSlotViewer(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(final DischargeSlotViewer pane) {
		pane.init(Arrays.asList(new EReference[] { CargoPackage.eINSTANCE.getCargoModel_DischargeSlots() }), getAdapterFactory(), getEditingDomain().getCommandStack());
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
			DischargeSlot dischargeSlot = null;
			if (dcsd.getTarget() instanceof DischargeSlot) {
				dischargeSlot = (DischargeSlot) dcsd.getTarget();
			}
			if (dischargeSlot != null) {
				getSite().getPage().activate(this);
				setSelection(new StructuredSelection(dischargeSlot), true);
			}
		}
	}
}
