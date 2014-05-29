/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.editor.views;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.StructuredSelection;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.ui.editorpart.VesselClassViewerPane;
import com.mmxlabs.models.lng.fleet.ui.editorpart.VesselViewerPane_View;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.lng.ui.views.MultiScenarioTableViewersView;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * Adds the assumption that the view will contain a {@link ScenarioTableViewerPane}
 * 
 * @author hinton
 * 
 */
public class VesselAndClassView extends MultiScenarioTableViewersView {

	private EReference[][] rootPaths = new EReference[][] { new EReference[] { LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_FleetModel(), FleetPackage.eINSTANCE.getFleetModel_Vessels() },
			new EReference[] { LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_FleetModel(), FleetPackage.eINSTANCE.getFleetModel_VesselClasses() } };

	private VesselClassViewerPane vesselClassViewerPane;
	private VesselViewerPane_View vesselViewerPane;

	protected List<ScenarioTableViewerPane> createViewerPanes() {
		vesselViewerPane = new VesselViewerPane_View(getSite().getPage(), this, this, getViewSite().getActionBars());
		vesselClassViewerPane = new VesselClassViewerPane(getSite().getPage(), this, this, getViewSite().getActionBars());

		List<ScenarioTableViewerPane> result = new LinkedList<ScenarioTableViewerPane>();
		result.add(vesselViewerPane);
		result.add(vesselClassViewerPane);
		return result;
	}

	@Override
	protected EReference[][] getPaneRootPaths() {
		return rootPaths;
	}

	@Override
	public void openStatus(final IStatus status) {
		if (status.isMultiStatus()) {
			// Try first element
			openStatus(status.getChildren()[0]);
		}

		if (status instanceof DetailConstraintStatusDecorator) {

			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			if (dcsd.getTarget() instanceof VesselClass || dcsd.getTarget() instanceof BaseFuel || dcsd.getTarget() instanceof BaseFuelCost) {
				getSite().getPage().activate(this);
				vesselClassViewerPane.getViewer().setSelection(new StructuredSelection(dcsd.getTarget()), true);
			} else if (dcsd.getTarget() instanceof Vessel) {
				getSite().getPage().activate(this);
				vesselViewerPane.getViewer().setSelection(new StructuredSelection(dcsd.getTarget()), true);
			}
		}
	}
}
