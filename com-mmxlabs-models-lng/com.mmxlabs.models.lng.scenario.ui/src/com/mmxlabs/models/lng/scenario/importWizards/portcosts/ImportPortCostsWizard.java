/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.portcosts;

import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportPage;
import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportWizard;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class ImportPortCostsWizard extends AbstractImportWizard {

	public ImportPortCostsWizard(final ScenarioInstance scenarioInstance, final String windowTitle) {
		super(scenarioInstance, windowTitle);
	}
	
	@Override
	protected ImportAction getImportAction(final ImportAction.ImportHooksProvider ihp, final IScenarioDataProvider scenarioDataProvider) {
		return new ImportPortCostsAction(ihp, ScenarioModelUtil.getCostModel(scenarioDataProvider));
	}

	@Override
	protected AbstractImportPage getImportPage(final String pageName, final ScenarioInstance currentScenario) {
		return new ImportPortCostsPage(pageName, currentScenario);
	}
}
