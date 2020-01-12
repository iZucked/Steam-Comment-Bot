/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.pricingcalendars;

import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportPage;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ImportPricingCalendarsPage extends AbstractImportPage {

	public ImportPricingCalendarsPage(final String pageName, final ScenarioInstance currentScenario) {
		super(pageName, currentScenario);
	}
	
	@Override
	public String getItemDescription() {
		return "pricing calendars";
	}
}
