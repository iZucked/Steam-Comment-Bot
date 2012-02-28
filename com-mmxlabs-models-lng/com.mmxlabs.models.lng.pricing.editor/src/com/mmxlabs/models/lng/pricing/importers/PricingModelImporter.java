/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;

public class PricingModelImporter implements ISubmodelImporter {

	@Override
	public Map<String, String> getRequiredInputs() {
		return Collections.emptyMap();
	}

	@Override
	public UUIDObject importModel(Map<String, CSVReader> inputs,
			IImportContext context) {
		final PricingModel pricing = PricingFactory.eINSTANCE.createPricingModel();
		return pricing;
	}

	@Override
	public void exportModel(UUIDObject model,
			Map<String, Collection<Map<String, String>>> output) {
		
	}

}
