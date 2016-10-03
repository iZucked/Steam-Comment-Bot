/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.CurrencyIndex;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;

/**
 */
public class PricingModelImporter implements ISubmodelImporter {
	private static final HashMap<String, String> inputs = new HashMap<String, String>();
	public static final String PRICE_CURVE_KEY = "PRICE_CURVES";
	public static final String CHARTER_CURVE_KEY = "CHARTER_CURVES";
	public static final String BASEFUEL_PRICING_KEY = "BF_PRICING";
	public static final String CURRENCY_CURVE_KEY = "CURRENCY_CURVES";
	public static final String CONVERSION_FACTORS_KEY = "CONVERSION_FACTORS";

	static {
		inputs.put(PRICE_CURVE_KEY, "Commodity Curves");
		inputs.put(CHARTER_CURVE_KEY, "Charter Curves");
		inputs.put(BASEFUEL_PRICING_KEY, "Base Fuel Curves");
		inputs.put(CURRENCY_CURVE_KEY, "Currency Curves");
		inputs.put(CONVERSION_FACTORS_KEY, "Conversion Factors");
	}

	@Inject
	private IImporterRegistry importerRegistry;

	private IClassImporter commodityIndexImporter;
	private IClassImporter charterIndexImporter;
	private IClassImporter baseFuelIndexImporter;
	private IClassImporter currencyIndexImporter;
	private IClassImporter conversionFactorsImporter;

	/**
	 */
	public PricingModelImporter() {
		final Activator activator = Activator.getDefault();
		if (activator != null) {

			importerRegistry = activator.getImporterRegistry();
			registryInit();
		}
	}

	@Inject
	private void registryInit() {
		if (importerRegistry != null) {
			commodityIndexImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getCommodityIndex());
			charterIndexImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getCharterIndex());
			baseFuelIndexImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getBaseFuelIndex());
			currencyIndexImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getCurrencyIndex());
			conversionFactorsImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getUnitConversion());
		}
	}

	@Override
	public Map<String, String> getRequiredInputs() {
		return inputs;
	}

	@Override
	public UUIDObject importModel(final Map<String, CSVReader> inputs, final IMMXImportContext context) {
		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();
		if (inputs.containsKey(PRICE_CURVE_KEY)) {
			importCommodityCurves(pricingModel, inputs.get(PRICE_CURVE_KEY), context);
		}
		if (inputs.containsKey(CHARTER_CURVE_KEY)) {
			importCharterCurves(pricingModel, inputs.get(CHARTER_CURVE_KEY), context);
		}
		if (inputs.containsKey(BASEFUEL_PRICING_KEY)) {
			importBaseFuelCurves(pricingModel, inputs.get(BASEFUEL_PRICING_KEY), context);
		}
		if (inputs.containsKey(CURRENCY_CURVE_KEY)) {
			importCurrencyCurves(pricingModel, inputs.get(CURRENCY_CURVE_KEY), context);
		}
		if (inputs.containsKey(CONVERSION_FACTORS_KEY)) {
			importConversionFactors(pricingModel, inputs.get(CONVERSION_FACTORS_KEY), context);
		}

		return pricingModel;
	}

	private void importCommodityCurves(final PricingModel pricing, final CSVReader csvReader, final IMMXImportContext context) {
		pricing.getCommodityIndices().addAll((Collection<? extends CommodityIndex>) commodityIndexImporter.importObjects(PricingPackage.eINSTANCE.getCommodityIndex(), csvReader, context));
	}

	private void importCurrencyCurves(final PricingModel pricing, final CSVReader csvReader, final IMMXImportContext context) {
		pricing.getCurrencyIndices().addAll((Collection<? extends CurrencyIndex>) currencyIndexImporter.importObjects(PricingPackage.eINSTANCE.getCurrencyIndex(), csvReader, context));
	}

	private void importCharterCurves(final PricingModel pricing, final CSVReader csvReader, final IMMXImportContext context) {
		pricing.getCharterIndices().addAll((Collection<? extends CharterIndex>) charterIndexImporter.importObjects(PricingPackage.eINSTANCE.getCharterIndex(), csvReader, context));
	}

	private void importBaseFuelCurves(final PricingModel pricing, final CSVReader csvReader, final IMMXImportContext context) {
		pricing.getBaseFuelPrices().addAll((Collection<? extends BaseFuelIndex>) baseFuelIndexImporter.importObjects(PricingPackage.eINSTANCE.getBaseFuelIndex(), csvReader, context));
	}

	private void importConversionFactors(final PricingModel pricing, final CSVReader csvReader, final IMMXImportContext context) {
		pricing.getConversionFactors().addAll((Collection<? extends UnitConversion>) conversionFactorsImporter.importObjects(PricingPackage.eINSTANCE.getUnitConversion(), csvReader, context));
	}

	@Override
	public void exportModel(final EObject model, final Map<String, Collection<Map<String, String>>> output, final IMMXExportContext context) {
		final PricingModel pricing = (PricingModel) model;
		output.put(PRICE_CURVE_KEY, commodityIndexImporter.exportObjects(pricing.getCommodityIndices(), context));
		output.put(CHARTER_CURVE_KEY, charterIndexImporter.exportObjects(pricing.getCharterIndices(), context));
		output.put(BASEFUEL_PRICING_KEY, baseFuelIndexImporter.exportObjects(pricing.getBaseFuelPrices(), context));
		output.put(CURRENCY_CURVE_KEY, currencyIndexImporter.exportObjects(pricing.getCurrencyIndices(), context));
		output.put(CONVERSION_FACTORS_KEY, conversionFactorsImporter.exportObjects(pricing.getConversionFactors(), context));
	}

	@Override
	public EClass getEClass() {
		return PricingPackage.eINSTANCE.getPricingModel();
	}
}
