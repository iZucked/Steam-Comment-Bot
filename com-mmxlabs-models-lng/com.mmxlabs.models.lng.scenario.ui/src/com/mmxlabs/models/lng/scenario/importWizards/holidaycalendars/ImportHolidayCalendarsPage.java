/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.holidaycalendars;

import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportPage;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 */
public class ImportHolidayCalendarsPage extends AbstractImportPage {

	public ImportHolidayCalendarsPage(final String pageName, final ScenarioInstance currentScenario, final boolean guided) {
		super(pageName, currentScenario, guided);
	}

	@Override
	public String getItemDescription() {
		return "holiday calendars";
	}
}
