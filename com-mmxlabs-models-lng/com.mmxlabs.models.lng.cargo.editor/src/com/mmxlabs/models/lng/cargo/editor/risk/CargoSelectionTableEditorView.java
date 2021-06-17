/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.risk;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.ui.editorpart.LightTradesWiringViewer;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.lng.ui.views.MultiScenarioTableViewersView;

public class CargoSelectionTableEditorView extends MultiScenarioTableViewersView{

	private final EReference[][] rootPaths = new EReference[][] {
		new EReference[] { LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_CargoModel(), CargoPackage.eINSTANCE.getCargoModel_Cargoes() } };
	
	private LightTradesWiringViewer cargoesPane;
	
	@Override
	protected EReference[][] getPaneRootPaths() {
		return rootPaths;
	}

	@Override
	protected List<ScenarioTableViewerPane> createViewerPanes() {
		final List<ScenarioTableViewerPane> result = new ArrayList<>();
		
		cargoesPane = new LightTradesWiringViewer(getSite().getPage(), this, this, getViewSite().getActionBars());
		
		result.add(cargoesPane);
		return result;
	}

	@Override
	public com.mmxlabs.models.ui.validation.IExtraValidationContext getExtraValidationContext() {
		return super.getExtraValidationContext();
	}

	@Override
	public com.mmxlabs.models.ui.validation.IStatusProvider getStatusProvider() {
		return super.getStatusProvider();
	}

}
