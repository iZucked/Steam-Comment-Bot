/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart.alternatives;

import java.util.Collections;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.lng.cargo.ui.editorpart.TradesWiringViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

public class DefaultTradesTableProvider implements IAlternativeEditorProvider {
	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public ScenarioTableViewerPane init(IWorkbenchPage page, IWorkbenchPart part, IScenarioEditingLocation scenarioEditingLocation, IActionBars actionBars, Composite parent, EObject modelObject) {
		TradesWiringViewer tradesViewer = new TradesWiringViewer(page, part, scenarioEditingLocation, actionBars);
		tradesViewer.createControl(parent);
		tradesViewer.init(Collections.<EReference> emptyList(), scenarioEditingLocation.getAdapterFactory(), scenarioEditingLocation.getModelReference());
		tradesViewer.getViewer().setInput(modelObject);
		return tradesViewer;
	}
}
