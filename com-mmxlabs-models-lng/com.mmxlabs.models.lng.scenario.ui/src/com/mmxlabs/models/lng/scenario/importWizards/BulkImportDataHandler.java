package com.mmxlabs.models.lng.scenario.importWizards;

import com.mmxlabs.models.lng.scenario.wizards.BulkImportPage;

public class BulkImportDataHandler extends BulkImportCSVHandler {

	@Override
	public int getFieldToImport() {
		// TODO Auto-generated method stub
		return BulkImportPage.CHOICE_COMMODITY_INDICES;
	}

}
