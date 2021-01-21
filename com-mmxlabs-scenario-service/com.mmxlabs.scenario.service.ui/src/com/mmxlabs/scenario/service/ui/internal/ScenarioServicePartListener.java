/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
				final ScenarioServiceSelectionProvider selectionProvider = Activator.getDefault().getScenarioServiceSelectionProvider();
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
				final ScenarioServiceSelectionProvider selectionProvider = Activator.getDefault().getScenarioServiceSelectionProvider();
				final ScenarioInstance pLastAutoSelection = lastAutoSelection;
				if (pLastAutoSelection != null && pLastAutoSelection != scenarioInstance) {
					if (selectionProvider.isSelected(pLastAutoSelection) && selectionProvider.getPinnedInstance() != pLastAutoSelection) {
						selectionProvider.deselect(pLastAutoSelection);
					}
					lastAutoSelection = null;
				}
				if (!selectionProvider.isSelected(scenarioInstance)) {
					try {
						// This line may fail if model cannot be loaded. Wrap everything up in exception handler
						final ScenarioResult scenarioResult = new ScenarioResult(scenarioInstance);
						selectionProvider.select(scenarioResult);
						lastAutoSelection = scenarioInstance;
					} catch (final Exception e) {
						// I don't think we really care here...
					}
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
				final ScenarioServiceSelectionProvider selectionProvider = Activator.getDefault().getScenarioServiceSelectionProvider();
				final ScenarioInstance pLastAutoSelection = lastAutoSelection;
				if (pLastAutoSelection != null && pLastAutoSelection != scenarioInstance) {
					if (selectionProvider.isSelected(pLastAutoSelection) && selectionProvider.getPinnedInstance() != pLastAutoSelection) {
						selectionProvider.deselect(pLastAutoSelection);
					}
					lastAutoSelection = null;
				}
				if (!selectionProvider.isSelected(scenarioInstance)) {
					try {
						// This line may fail if model cannot be loaded. Wrap everything up in exception handler
						final ScenarioResult scenarioResult = new ScenarioResult(scenarioInstance);
						selectionProvider.select(scenarioResult);
						lastAutoSelection = scenarioInstance;
					} catch (final Exception e) {
						// I don't think we really care here...
					}
				}
			}
		}
	}

}
