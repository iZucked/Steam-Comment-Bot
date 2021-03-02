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

/**
 * Part listener to keep track of the active editor
 * 
 * @author sg
 *
 */
public class ScenarioServicePartListener implements IPartListener {

	private ScenarioInstance lastAutoSelection = null;

	@Override
	public void partOpened(final IWorkbenchPart part) {
		// Nothing to do here
	}

	@Override
	public void partDeactivated(final IWorkbenchPart part) {
		// Nothing to do here
	}

	@Override
	public void partClosed(final IWorkbenchPart part) {
		// If the selection tracks editor, then get the scenario instance and make it the only selection.
		if (part instanceof IEditorPart) {
			final IEditorPart editorPart = (IEditorPart) part;
			final IEditorInput editorInput = editorPart.getEditorInput();
			final ScenarioInstance scenarioInstance = editorInput.getAdapter(ScenarioInstance.class);
			if (scenarioInstance != null && lastAutoSelection == scenarioInstance) {
				// Nothing to do here
				final IScenarioServiceSelectionProvider selectionProvider = Activator.getDefault().getScenarioServiceSelectionProvider();
				if (selectionProvider != null) {
					selectionProvider.updateActiveEditorScenario(null, lastAutoSelection, false);
					lastAutoSelection = null;
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
			final ScenarioInstance scenarioInstance = editorInput.getAdapter(ScenarioInstance.class);
			final IScenarioServiceSelectionProvider selectionProvider = Activator.getDefault().getScenarioServiceSelectionProvider();

			if (selectionProvider != null) {
				selectionProvider.updateActiveEditorScenario(scenarioInstance, lastAutoSelection, false);
				lastAutoSelection = scenarioInstance;
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
					selectionProvider.updateActiveEditorScenario(scenarioInstance, lastAutoSelection, false);
					lastAutoSelection = scenarioInstance;
				}
			}
		}
	}

}
