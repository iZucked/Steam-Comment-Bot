/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.paperdeals;

import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportPage;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ImportPaperDealsPage extends AbstractImportPage {

	public ImportPaperDealsPage(final String pageName, final ScenarioInstance currentScenario) {
		super(pageName, currentScenario);
	}
	
	@Override
	public String getItemDescription() {
		return "paper deals";
	}
}
