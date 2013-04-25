/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.editor.views;

import java.util.Arrays;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.StructuredSelection;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.ui.editorpart.VesselClassViewerPane;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class VesselClassView extends ScenarioTableViewerView<VesselClassViewerPane> {
	@Override
	protected VesselClassViewerPane createViewerPane() {
		return new VesselClassViewerPane(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(final VesselClassViewerPane pane) {
		pane.init(Arrays.asList(new EReference[] { LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_FleetModel(), FleetPackage.eINSTANCE.getFleetModel_VesselClasses() }), getAdapterFactory(),
				getEditingDomain().getCommandStack());
		pane.getViewer().setInput(getRootObject());
	}

	@Override
	public void openStatus(final IStatus status) {
		if (status.isMultiStatus()) {
			// Try first element
			openStatus(status.getChildren()[0]);
		}

		if (status instanceof DetailConstraintStatusDecorator) {

			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			VesselClass object = null;
			if (dcsd.getTarget() instanceof VesselClass) {
				object = (VesselClass) dcsd.getTarget();
			}
			if (object != null) {
				getSite().getPage().activate(this);
				setSelection(new StructuredSelection(object), true);
			}
		}
	}

}
