/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.editor.views;

import java.util.Arrays;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.StructuredSelection;

import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.ui.editorpart.VesselViewerPane_View;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class VesselView extends ScenarioTableViewerView<VesselViewerPane_View> {
	@Override
	protected VesselViewerPane_View createViewerPane() {
		return new VesselViewerPane_View(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(final VesselViewerPane_View pane) {
		pane.init(Arrays.asList(new EReference[] { FleetPackage.eINSTANCE.getFleetModel_Vessels() }), getAdapterFactory(), getEditingDomain().getCommandStack());
		pane.getViewer().setInput(getRootObject().getSubModel(FleetModel.class));
	}

	@Override
	public void openStatus(final IStatus status) {
		if (status.isMultiStatus()) {
			// Try first element
			openStatus(status.getChildren()[0]);
		}

		if (status instanceof DetailConstraintStatusDecorator) {

			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			Vessel object = null;
			if (dcsd.getTarget() instanceof Vessel) {
				object = (Vessel) dcsd.getTarget();
			}
			if (object != null) {
				getSite().getPage().activate(this);
				setSelection(new StructuredSelection(object), true);
			}
		}
	}
}
