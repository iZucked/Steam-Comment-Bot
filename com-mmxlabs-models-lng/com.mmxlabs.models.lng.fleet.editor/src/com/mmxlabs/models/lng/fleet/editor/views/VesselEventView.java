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
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.fleet.ui.editorpart.VesselEventViewerPane;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class VesselEventView extends ScenarioTableViewerView<VesselEventViewerPane> {

	@Override
	protected VesselEventViewerPane createViewerPane() {
		return new VesselEventViewerPane(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(final VesselEventViewerPane pane) {
		pane.init(Arrays.asList(new EReference[] { FleetPackage.eINSTANCE.getFleetModel_VesselEvents() }), getAdapterFactory(), getEditingDomain().getCommandStack());
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
			VesselEvent object = null;
			if (dcsd.getTarget() instanceof VesselEvent) {
				object = (VesselEvent) dcsd.getTarget();
			}
			if (object != null) {
				getSite().getPage().activate(this);
				setSelection(new StructuredSelection(object), true);
			}
		}
	}
}
