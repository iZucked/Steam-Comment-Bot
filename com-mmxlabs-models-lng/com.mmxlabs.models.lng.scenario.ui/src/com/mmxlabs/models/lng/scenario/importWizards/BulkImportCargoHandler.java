/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import com.mmxlabs.models.lng.scenario.wizards.BulkImportPage;

public class BulkImportCargoHandler extends BulkImportCSVHandler {

	@Override
	public int getFieldToImport() {
		return BulkImportPage.CHOICE_CARGOES;
	}

}
