/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.pricingcalendars;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.ui.IWorkbenchWindow;

import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportHandler;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class ImportPricingCalendarsHandler extends AbstractImportHandler {

	@Override
	public IWizard getImportWizard(final IScenarioServiceEditorInput editor, final IWorkbenchWindow activeWorkbenchWindow) {
		final ImportPricingCalendarsWizard wizard = new ImportPricingCalendarsWizard(editor != null ? editor.getScenarioInstance() : null, "Import pricing calendars");
		wizard.init(activeWorkbenchWindow.getWorkbench(), null);
		return wizard;
	}
}
