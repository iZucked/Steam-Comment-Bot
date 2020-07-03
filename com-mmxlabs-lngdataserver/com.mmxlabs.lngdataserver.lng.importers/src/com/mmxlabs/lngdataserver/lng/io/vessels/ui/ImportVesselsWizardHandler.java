/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.io.vessels.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class ImportVesselsWizardHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if (activeWorkbenchWindow == null) {
			// action has been disposed
			return null;
		}

		final IEditorInput activeEditorInput = HandlerUtil.getActiveEditorInput(event);
		ScenarioInstance currentInstance = null;
		if (activeEditorInput instanceof IScenarioServiceEditorInput) {
			final IScenarioServiceEditorInput editorInput = (IScenarioServiceEditorInput) activeEditorInput;
			currentInstance = editorInput.getScenarioInstance();
		}
		VesselsToScenarioImportWizard wizard = new VesselsToScenarioImportWizard(null, currentInstance, false);

		wizard.init(activeWorkbenchWindow.getWorkbench(), null);

		Shell parent = activeWorkbenchWindow.getShell();

		final WizardDialog dialog = new WizardDialog(parent, wizard);
		dialog.create();
		dialog.open();

		return null;
	}

}
