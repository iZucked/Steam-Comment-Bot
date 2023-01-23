/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.nominations;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.ui.IWorkbenchWindow;

import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportHandler;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class ImportNominationsHandler extends AbstractImportHandler {
	
	@Override
	public IWizard getImportWizard(final IScenarioServiceEditorInput editor, final IWorkbenchWindow activeWorkbenchWindow) {
		final ImportNominationsWizard wizard = new ImportNominationsWizard(editor != null ? editor.getScenarioInstance() : null, "Import nominations");
		wizard.init(activeWorkbenchWindow.getWorkbench(), null);
		return wizard;
	}
}
