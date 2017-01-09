/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.mergeWizards;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class SharedScenarioDataImportWizardHandler extends AbstractHandler {

	protected IStructuredSelection getSelectionToUse(final ExecutionEvent event) {
		final ISelection selection = HandlerUtil.getCurrentSelection(event);
		IStructuredSelection selectionToPass = StructuredSelection.EMPTY;
		if (selection instanceof IStructuredSelection) {
			selectionToPass = (IStructuredSelection) selection;
		}
		return selectionToPass;
	}

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if (activeWorkbenchWindow == null) {
			// action has been disposed
			return null;
		}

		final IEditorInput activeEditorInput = HandlerUtil.getActiveEditorInput(event);
		ScenarioInstance currentInstance = null;
		if (activeEditorInput instanceof IScenarioServiceEditorInput) {
			final IScenarioServiceEditorInput editorInput = (IScenarioServiceEditorInput) HandlerUtil.getActiveEditorInput(event);
			currentInstance = editorInput.getScenarioInstance();
		}
		final SharedScenarioDataImportWizard wizard = new SharedScenarioDataImportWizard(currentInstance);

		final IStructuredSelection selectionToPass = getSelectionToUse(event);
		wizard.init(activeWorkbenchWindow.getWorkbench(), selectionToPass);

		wizard.setForcePreviousAndNextButtons(true);

		final Shell parent = activeWorkbenchWindow.getShell();
		final WizardDialog dialog = new WizardDialog(parent, wizard);
		dialog.create();
		dialog.open();

		return null;
	}
}
