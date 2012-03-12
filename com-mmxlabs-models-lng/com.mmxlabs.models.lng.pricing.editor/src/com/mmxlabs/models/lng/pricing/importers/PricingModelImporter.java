/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.models.lng.pricing.FleetCostModel;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;

public class PricingModelImporter implements ISubmodelImporter {
	private static final HashMap<String, String> inputs = new HashMap<String, String>();
	private static final String PRICE_CURVE_KEY = "PRICE_CURVES";
	private static final String CHARTER_CURVE_KEY = "CHARTER_CURVES";
	
	static {
		inputs.put(PRICE_CURVE_KEY, "Commodity Curves");
		inputs.put(CHARTER_CURVE_KEY, "Charter Curves");
	}
	
	final IClassImporter dataIndexImporter = 
			Activator.getDefault().getImporterRegistry().getClassImporter(PricingPackage.eINSTANCE.getDataIndex());
	
	@Override
	public Map<String, String> getRequiredInputs() {
		return inputs;
	}

	@Override
	public UUIDObject importModel(final Map<String, CSVReader> inputs, final IImportContext context) {
		final PricingModel pricing = PricingFactory.eINSTANCE.createPricingModel();
		if (inputs.containsKey(PRICE_CURVE_KEY)) importCommodityCurves(pricing, inputs.get(PRICE_CURVE_KEY), context);
		if (inputs.containsKey(CHARTER_CURVE_KEY)) importCharterCurves(pricing, inputs.get(CHARTER_CURVE_KEY), context);
		
		final FleetCostModel fcm = PricingFactory.eINSTANCE.createFleetCostModel();
		pricing.setFleetCost(fcm);
		
		// blah blah.
		
		return pricing;
	}

	private void importCommodityCurves(final PricingModel pricing, final CSVReader csvReader, final IImportContext context) {
		if (dataIndexImporter instanceof DataIndexImporter) {
			((DataIndexImporter) dataIndexImporter).setParseAsInt(false);
		}
		pricing.getCommodityIndices().addAll((Collection<? extends Index<Double>>) dataIndexImporter.importObjects(PricingPackage.eINSTANCE.getDataIndex(), csvReader, context));
	}

	private void importCharterCurves(final PricingModel pricing, final CSVReader csvReader, IImportContext context) {
		if (dataIndexImporter instanceof DataIndexImporter) {
			((DataIndexImporter) dataIndexImporter).setParseAsInt(true);
		}
		pricing.getCharterIndices().addAll((Collection<? extends Index<Integer>>) dataIndexImporter.importObjects(PricingPackage.eINSTANCE.getDataIndex(), csvReader, context));
	}

	@Override
	public void exportModel(UUIDObject model,
			Map<String, Collection<Map<String, String>>> output) {
		//TODO exporter
	}

}
