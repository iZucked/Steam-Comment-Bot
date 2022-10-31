/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import com.mmxlabs.models.lng.scenario.wizards.BulkImportPage.FieldChoice;

public class BulkImportCurrencyCurveHandler extends BulkImportCSVCurvesHandler {

	@Override
	public FieldChoice getFieldToImport() {
		return FieldChoice.CHOICE_CURRENCY_CURVES;
	}

	@Override
	public String getTitle() {
		return "Import currency curves";
	}

}
