/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.pricing.importers.PricingImportConstants;
import com.mmxlabs.models.lng.scenario.wizards.BulkImportPage.FieldChoice;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public abstract class BulkImportCSVCurvesHandler extends BulkImportCSVHandler {

	@Override
	protected BulkImportWizard createBulkImportWizard(@Nullable final ScenarioInstance scenarioInstance, final FieldChoice fieldToImport, final String title) {
		return new BulkImportWizard(scenarioInstance, fieldToImport, title, si -> {
			final IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
			eventBroker.post(PricingImportConstants.LOADED_PRICE_CURVES, si);
		});
	}
}
