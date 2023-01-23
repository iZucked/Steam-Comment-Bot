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

import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.CargoBulkEditorFactory;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.CargoBulkEditorPackage;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Table;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.BulkTradesTablePane;
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

		Table table = CargoBulkEditorFactory.eINSTANCE.createTable();
		tradesViewer.setTable(table);
		tradesViewer.setlngScenarioModel((LNGScenarioModel) scenarioEditingLocation.getRootObject());
		tradesViewer.init(Arrays.asList(new EReference[] { CargoBulkEditorPackage.eINSTANCE.getTable_Rows() }), null, scenarioEditingLocation.getModelReference());
		tradesViewer.setCargoes(table, (LNGScenarioModel) scenarioEditingLocation.getRootObject());
		tradesViewer.setlngScenarioModel(((LNGScenarioModel) scenarioEditingLocation.getRootObject()));
		tradesViewer.setInput(table);
		return tradesViewer;
	}
}
