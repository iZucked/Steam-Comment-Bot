/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.portcosts;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.ui.IWorkbenchWindow;

import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportHandler;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

/*
 * Author FM
 */

public class ImportPortCostsHandler extends AbstractImportHandler {

	@Override
	public IWizard getImportWizard(final IScenarioServiceEditorInput editor, final IWorkbenchWindow activeWorkbenchWindow) {
		final ImportPortCostsWizard wizard = new ImportPortCostsWizard(editor != null ? editor.getScenarioInstance() : null, "Import port costs");
		wizard.init(activeWorkbenchWindow.getWorkbench(), null);
		return wizard;
	}
}