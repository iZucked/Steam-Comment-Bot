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
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.LoadSlotViewer;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class LoadSlotsView extends ScenarioTableViewerView<LoadSlotViewer> {
	public static final String ID = "com.mmxlabs.models.lng.cargo.editor.LoadSlotsView";

	@Override
	protected LoadSlotViewer createViewerPane() {
		return new LoadSlotViewer(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(final LoadSlotViewer pane) {
		pane.init(Arrays.asList(new EReference[] { CargoPackage.eINSTANCE.getCargoModel_LoadSlots() }), getAdapterFactory(), getEditingDomain().getCommandStack());
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
			LoadSlot loadSlot = null;
			if (dcsd.getTarget() instanceof LoadSlot) {
				loadSlot = (LoadSlot) dcsd.getTarget();
			}
			if (loadSlot != null) {
				getSite().getPage().activate(this);
				setSelection(new StructuredSelection(loadSlot), true);
			}
		}
	}
}
