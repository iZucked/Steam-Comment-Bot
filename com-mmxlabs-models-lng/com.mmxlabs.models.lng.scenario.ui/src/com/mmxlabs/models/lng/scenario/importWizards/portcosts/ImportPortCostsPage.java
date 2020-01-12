/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.portcosts;

import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportPage;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ImportPortCostsPage extends AbstractImportPage {

	public ImportPortCostsPage(final String pageName, final ScenarioInstance currentScenario) {
		super(pageName, currentScenario);
	}
	
	@Override
	public String getItemDescription() {
		return "port costs";
	}
}
