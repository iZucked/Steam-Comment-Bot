/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import com.mmxlabs.models.lng.scenario.wizards.BulkImportPage.FieldChoice;

public class BulkImportCargoHandler extends BulkImportCSVHandler {

	@Override
	public FieldChoice getFieldToImport() {
		return FieldChoice.CHOICE_CARGOES;
	}

}
