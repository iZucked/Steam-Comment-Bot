/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.excel;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.ui.IWorkbenchWindow;

import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportHandler;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class ImportPaperDealsFromExcelHandler extends AbstractImportHandler {

	@Override
	public IWizard getImportWizard(final IScenarioServiceEditorInput editor, final IWorkbenchWindow activeWorkbenchWindow) {
		final ImportPaperDealsFromExcelWizard wizard = new ImportPaperDealsFromExcelWizard(editor != null ? editor.getScenarioInstance() : null, "Import paper deals from Excel");
		wizard.init(activeWorkbenchWindow.getWorkbench(), null);
		return wizard;
	}
}
