/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import com.mmxlabs.models.lng.scenario.wizards.BulkImportPage.FieldChoice;

public class BulkImportCharterCurveHandler extends BulkImportCSVCurvesHandler {

	@Override
	public FieldChoice getFieldToImport() {
		return FieldChoice.CHOICE_CHARTER_INDICES;
	}

	@Override
	public String getTitle() {
		return "Import charter curves";
	}
}
