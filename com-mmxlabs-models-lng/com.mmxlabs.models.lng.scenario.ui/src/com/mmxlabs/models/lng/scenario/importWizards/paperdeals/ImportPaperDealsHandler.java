/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.paperdeals;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.ui.IWorkbenchWindow;

import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportHandler;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class ImportPaperDealsHandler extends AbstractImportHandler {

	@Override
	public IWizard getImportWizard(final IScenarioServiceEditorInput editor, final IWorkbenchWindow activeWorkbenchWindow) {
		final ImportPaperDealsWizard wizard = new ImportPaperDealsWizard(editor != null ? editor.getScenarioInstance() : null, "Import paper deals");
		wizard.init(activeWorkbenchWindow.getWorkbench(), null);
		return wizard;
	}
}
