/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.editor.views;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.ui.editorpart.VesselClassViewerPane;
import com.mmxlabs.models.lng.fleet.ui.editorpart.VesselViewerPane_View;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.lng.ui.views.MultiScenarioTableViewersView;

/**
 * Adds the assumption that the view will contain a {@link ScenarioTableViewerPane}
 * 
 * @author hinton
 * 
 */
public class VesselAndClassView extends MultiScenarioTableViewersView {

	private EReference [] [] rootPaths = new EReference [] [] { 
			new EReference [] { LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_FleetModel(), FleetPackage.eINSTANCE.getFleetModel_Vessels() },
			new EReference [] { LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_FleetModel(), FleetPackage.eINSTANCE.getFleetModel_VesselClasses() }
	}; 

	protected List<ScenarioTableViewerPane> createViewerPanes() {
		LinkedList<ScenarioTableViewerPane> result = new LinkedList<ScenarioTableViewerPane>();
		result.add(new VesselViewerPane_View(getSite().getPage(), this, this, getViewSite().getActionBars()));
		result.add(new VesselClassViewerPane(getSite().getPage(), this, this, getViewSite().getActionBars()));
		return result;
	}

	@Override
	protected EReference[][] getPaneRootPaths() {
		return rootPaths;
	}

}
