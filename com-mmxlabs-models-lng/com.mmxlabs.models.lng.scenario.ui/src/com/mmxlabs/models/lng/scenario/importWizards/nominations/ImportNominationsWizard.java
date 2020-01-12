/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.nominations;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;

import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportPage;
import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportWizard;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class ImportNominationsWizard extends AbstractImportWizard {

	public ImportNominationsWizard(final ScenarioInstance scenarioInstance, final String windowTitle) {
		super(scenarioInstance, windowTitle);
	}	
	
	@Override
	protected ImportAction getImportAction(final ImportAction.ImportHooksProvider ihp, final IScenarioDataProvider scenarioDataProvider) {
		return new NominationsImportAction(ihp, ScenarioModelUtil.getNominationsModel(scenarioDataProvider));
	}

	@Override
	protected AbstractImportPage getImportPage(final String pageName, final ScenarioInstance currentScenario) {
		return new ImportNominationsPage(pageName, currentScenario);
	}
	
	@Override
	protected void displayWarningDialog(final List<String> allProblems) {
		// pop up a dialog showing the problems
		final StringBuilder sb = new StringBuilder();
		if (allProblems.contains("Duplicate nomination")) {
			sb.append("Some duplicate nominations were detected during csv import - these were ignored as already present in the model.\n");
		}
		else {
			sb.append("There were problems with the import (perhaps a wrong delimiter character was used): \n");
			for (final String problem : allProblems) {
				sb.append("\n- ");
				sb.append(problem);
			}
		}
		sb.append("\nPlease check the error log for more details.");
		MessageDialog.openWarning(getShell(), "Import Problems", sb.toString());
	}
}
