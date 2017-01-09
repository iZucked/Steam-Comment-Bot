/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.navigator.ILinkHelper;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.editing.ScenarioServiceEditorInput;

public class ScenarioServiceLinkHelper implements ILinkHelper {

	@Override
	public IStructuredSelection findSelection(final IEditorInput anInput) {
		return new StructuredSelection(anInput.getAdapter(ScenarioInstance.class));
	}

	@Override
	public void activateEditor(final IWorkbenchPage aPage, final IStructuredSelection aSelection) {

		final Object firstElement = aSelection.getFirstElement();
		if (firstElement instanceof ScenarioInstance) {

			final ScenarioInstance scenarioInstance = (ScenarioInstance) firstElement;
			final ScenarioServiceEditorInput editorInput = new ScenarioServiceEditorInput(scenarioInstance);
			final IEditorPart editorPart = aPage.findEditor(editorInput);
			if (editorPart != null) {
				aPage.bringToTop(editorPart);
			}
		}
	}
}
