/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public abstract class AbstractImportHandler extends AbstractHandler {

	public abstract IWizard getImportWizard(final IScenarioServiceEditorInput editor, final IWorkbenchWindow activeWorkbenchWindow);
	
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if (activeWorkbenchWindow == null) {
			// action has been disposed
			return null;
		}

		final Shell shell = HandlerUtil.getActiveShell(event);
		final IScenarioServiceEditorInput editor = (IScenarioServiceEditorInput) HandlerUtil.getActiveEditorInput(event);

		final IWizard wizard = getImportWizard(editor, activeWorkbenchWindow);
	
		final WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		dialog.open();

		return null;
	}
}