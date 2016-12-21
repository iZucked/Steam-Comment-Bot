/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.internal;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class ScenarioServicePartListener implements IPartListener {

	private ScenarioInstance lastAutoSelection = null;

	@Override
	public void partOpened(final IWorkbenchPart part) {

	}

	@Override
	public void partDeactivated(final IWorkbenchPart part) {

	}

	@Override
	public void partClosed(final IWorkbenchPart part) {
		// If the selection tracks editor, then get the scenario instance and make it the only selection.
		if (part instanceof IEditorPart) {
			final IEditorPart editorPart = (IEditorPart) part;
			final IEditorInput editorInput = editorPart.getEditorInput();
			final ScenarioInstance scenarioInstance = (ScenarioInstance) editorInput.getAdapter(ScenarioInstance.class);
			if (scenarioInstance != null) {
				ScenarioServiceSelectionProvider selectionProvider = Activator.getDefault().getScenarioServiceSelectionProvider();
				if (lastAutoSelection == scenarioInstance) {
					lastAutoSelection = null;
					if (selectionProvider.isSelected(scenarioInstance) && scenarioInstance != selectionProvider.getPinnedInstance()) {
						selectionProvider.deselect(scenarioInstance);
					}
				}
			}
		}
	}

	@Override
	public void partBroughtToTop(final IWorkbenchPart part) {
		// If the selection tracks editor, then get the scenario instance and make it the only selection.
		if (part instanceof IEditorPart) {
			final IEditorPart editorPart = (IEditorPart) part;
			final IEditorInput editorInput = editorPart.getEditorInput();
			final ScenarioInstance scenarioInstance = (ScenarioInstance) editorInput.getAdapter(ScenarioInstance.class);
			if (scenarioInstance != null) {
				ScenarioServiceSelectionProvider selectionProvider = Activator.getDefault().getScenarioServiceSelectionProvider();
				if (lastAutoSelection != null && lastAutoSelection != scenarioInstance) {
					if (selectionProvider.isSelected(lastAutoSelection) && selectionProvider.getPinnedInstance() != lastAutoSelection) {
						selectionProvider.deselect(lastAutoSelection);
					}
					lastAutoSelection = null;
				}
				if (!selectionProvider.isSelected(scenarioInstance)) {
					lastAutoSelection = scenarioInstance;
					selectionProvider.select(new ScenarioResult(scenarioInstance));
				}
			}
		}
	}

	@Override
	public void partActivated(final IWorkbenchPart part) {
		// If the selection tracks editor, then get the scenario instance and make it the only selection.
		if (part instanceof IEditorPart) {
			final IEditorPart editorPart = (IEditorPart) part;
			final IEditorInput editorInput = editorPart.getEditorInput();
			final ScenarioInstance scenarioInstance = (ScenarioInstance) editorInput.getAdapter(ScenarioInstance.class);
			if (scenarioInstance != null) {
				ScenarioServiceSelectionProvider selectionProvider = Activator.getDefault().getScenarioServiceSelectionProvider();
				if (lastAutoSelection != null && lastAutoSelection != scenarioInstance) {
					if (selectionProvider.isSelected(lastAutoSelection) && selectionProvider.getPinnedInstance() != lastAutoSelection) {
						selectionProvider.deselect(lastAutoSelection);
					}
					lastAutoSelection = null;
				}
				if (!selectionProvider.isSelected(scenarioInstance)) {
					lastAutoSelection = scenarioInstance;
					selectionProvider.select(new ScenarioResult(scenarioInstance));
				}
			}
		}
	}

}
