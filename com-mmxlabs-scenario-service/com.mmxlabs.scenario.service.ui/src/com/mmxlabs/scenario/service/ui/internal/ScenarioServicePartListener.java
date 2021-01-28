/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.internal;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.scenario.service.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioResultImpl;

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
			final ScenarioInstance scenarioInstance = editorInput.getAdapter(ScenarioInstance.class);
			if (scenarioInstance != null) {
				final IScenarioServiceSelectionProvider selectionProvider = Activator.getDefault().getScenarioServiceSelectionProvider();
				if (lastAutoSelection == scenarioInstance) {
					lastAutoSelection = null;
					ScenarioResult pinned = selectionProvider.getPinned();

					if (selectionProvider.isSelected(scenarioInstance) && (pinned == null || scenarioInstance != pinned.getScenarioInstance())) {
						selectionProvider.deselect(scenarioInstance, false);
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
				final IScenarioServiceSelectionProvider selectionProvider = Activator.getDefault().getScenarioServiceSelectionProvider();
				final ScenarioInstance pLastAutoSelection = lastAutoSelection;
				if (pLastAutoSelection != null && pLastAutoSelection != scenarioInstance) {
					ScenarioResult pinned = selectionProvider.getPinned();
					if (selectionProvider.isSelected(pLastAutoSelection) && (pinned == null || pinned.getScenarioInstance() != pLastAutoSelection)) {
						selectionProvider.deselect(pLastAutoSelection, false);
					}
					lastAutoSelection = null;
				}
				if (!selectionProvider.isSelected(scenarioInstance)) {
					try {
						// This line may fail if model cannot be loaded. Wrap everything up in exception handler
						final ScenarioResult scenarioResult = new ScenarioResultImpl(scenarioInstance);
						selectionProvider.select(scenarioResult, false);
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
			final ScenarioInstance scenarioInstance = editorInput.getAdapter(ScenarioInstance.class);
			if (scenarioInstance != null) {
				final IScenarioServiceSelectionProvider selectionProvider = Activator.getDefault().getScenarioServiceSelectionProvider();

				if (selectionProvider != null) {
					final ScenarioInstance pLastAutoSelection = lastAutoSelection;
					if (pLastAutoSelection != null && pLastAutoSelection != scenarioInstance) {
						if (selectionProvider.isSelected(pLastAutoSelection) && selectionProvider.getPinned() != pLastAutoSelection) {
							selectionProvider.deselect(pLastAutoSelection, false);
						}
						lastAutoSelection = null;
					}
					if (!selectionProvider.isSelected(scenarioInstance)) {
						try {
							// This line may fail if model cannot be loaded. Wrap everything up in exception handler
							final ScenarioResult scenarioResult = new ScenarioResultImpl(scenarioInstance);
							selectionProvider.select(scenarioResult, false);
							lastAutoSelection = scenarioInstance;
						} catch (final Exception e) {
							// I don't think we really care here...
						}
					}
				}
			}
		}
	}

}
