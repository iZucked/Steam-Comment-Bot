/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import com.mmxlabs.models.lng.scenario.wizards.BulkImportPage;

public class BulkImportDataHandler extends BulkImportCSVHandler {

	@Override
	public int getFieldToImport() {
		return BulkImportPage.CHOICE_COMMODITY_INDICES;
	}

}
