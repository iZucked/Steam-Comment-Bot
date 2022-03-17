/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.holidaycalendars;

import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportPage;
import com.mmxlabs.models.lng.scenario.importWizards.AbstractImportWizard;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.models.lng.ui.actions.ImportAction.ImportHooksProvider;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

/**
 */
public class ImportHolidayCalendarsWizard extends AbstractImportWizard{
	
	public ImportHolidayCalendarsWizard(final ScenarioInstance scenarioInstance, final String windowTitle) {
		super(scenarioInstance, windowTitle);
	}
	
	public ImportHolidayCalendarsWizard(final ScenarioInstance scenarioInstance, final String windowTitle, final String importFilename) {
		super(scenarioInstance, windowTitle, importFilename);
	}

	@Override
	protected AbstractImportPage getImportPage(String pageName, ScenarioInstance currentScenario) {
		return new ImportHolidayCalendarsPage(pageName, currentScenario, this.guided);
	}

	@Override
	protected ImportAction getImportAction(ImportHooksProvider ihp, IScenarioDataProvider scenarioDataProvider) {
		return new HolidayCalendarsImportAction(ihp, ScenarioModelUtil.getPricingModel(scenarioDataProvider));
	}
}
