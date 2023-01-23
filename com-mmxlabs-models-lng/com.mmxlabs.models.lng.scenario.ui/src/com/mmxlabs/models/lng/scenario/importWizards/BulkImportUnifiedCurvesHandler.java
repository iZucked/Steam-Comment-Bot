/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import com.mmxlabs.models.lng.scenario.wizards.BulkImportPage.FieldChoice;

public class BulkImportUnifiedCurvesHandler extends BulkImportCSVCurvesHandler {

	@Override
	public FieldChoice getFieldToImport() {
		return FieldChoice.CHOICE_ALL_INDICIES;
	}

	@Override
	public String getTitle() {
		return "Import market curves";
	}
}
