/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.ui;

import java.util.Arrays;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.BulkTradesTablePane;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelFactory;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesTableRoot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.alternatives.IAlternativeEditorProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

public class BulkTradesTableProvider implements IAlternativeEditorProvider {

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public ScenarioTableViewerPane init(IWorkbenchPage page, IWorkbenchPart part, IScenarioEditingLocation scenarioEditingLocation, IActionBars actionBars, Composite parent, EObject modelObject) {
		BulkTradesTablePane tradesViewer = new BulkTradesTablePane(page, part, scenarioEditingLocation, actionBars);
		tradesViewer.createControl(parent);

		TradesTableRoot tradesTableRoot = CargoEditorModelFactory.eINSTANCE.createTradesTableRoot();
		tradesViewer.setTradesTableRoot(tradesTableRoot);
		tradesViewer.setlngScenarioModel((LNGScenarioModel) scenarioEditingLocation.getRootObject());
		tradesViewer.init(Arrays.asList(new EReference[] { CargoEditorModelPackage.eINSTANCE.getTradesTableRoot_TradesRows() }), null, scenarioEditingLocation.getModelReference());
		tradesViewer.setCargoes(tradesTableRoot, (LNGScenarioModel) scenarioEditingLocation.getRootObject());
		tradesViewer.setlngScenarioModel(((LNGScenarioModel) scenarioEditingLocation.getRootObject()));
		tradesViewer.setInput(tradesTableRoot);
		return tradesViewer;
	}
}
