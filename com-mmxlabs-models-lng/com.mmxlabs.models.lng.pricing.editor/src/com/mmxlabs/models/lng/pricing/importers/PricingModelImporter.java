/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.pricing.BunkerFuelCurve;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.CurrencyCurve;
import com.mmxlabs.models.lng.pricing.DatePointContainer;
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
	public static final String SETTLED_PRICES_KEY = "SETTLED_PRICES";

	static {
		inputs.put(PRICE_CURVE_KEY, "Commodity Curves");
		inputs.put(CHARTER_CURVE_KEY, "Charter Curves");
		inputs.put(BASEFUEL_PRICING_KEY, "Base Fuel Curves");
		inputs.put(CURRENCY_CURVE_KEY, "Currency Curves");
		if (LicenseFeatures.isPermitted("features:paperdeals")) {
			inputs.put(SETTLED_PRICES_KEY, "Settled Prices");
		}
		inputs.put(CONVERSION_FACTORS_KEY, "Conversion Factors");
	}

	@Inject
	private IImporterRegistry importerRegistry;

	private IClassImporter commodityIndexImporter;
	private IClassImporter charterIndexImporter;
	private IClassImporter baseFuelIndexImporter;
	private IClassImporter currencyIndexImporter;
	private IClassImporter conversionFactorsImporter;
	private IClassImporter settledPricesImporter;

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
			commodityIndexImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getCommodityCurve());
			charterIndexImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getCharterCurve());
			baseFuelIndexImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getBunkerFuelCurve());
			currencyIndexImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getCurrencyCurve());
			conversionFactorsImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getUnitConversion());
			settledPricesImporter = new DatePointImporter();
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
		if (inputs.containsKey(SETTLED_PRICES_KEY)) {
			importSettledPrices(pricingModel, inputs.get(SETTLED_PRICES_KEY), context);
		}
		if (inputs.containsKey(CONVERSION_FACTORS_KEY)) {
			importConversionFactors(pricingModel, inputs.get(CONVERSION_FACTORS_KEY), context);
		}
		if (pricingModel.getConversionFactors().isEmpty()) {
			// Create default conversion factors
			makeConversionFactor("therm", "mmBtu", 10, pricingModel);
			makeConversionFactor("bbl", "mmBtu", 0.180136, pricingModel);
			makeConversionFactor("MWh", "mmBtu", 0.293297, pricingModel);

		}

		pricingModel.setMarketCurveDataVersion(EcoreUtil.generateUUID());

		return pricingModel;
	}

	private void importCommodityCurves(final PricingModel pricing, final CSVReader csvReader, final IMMXImportContext context) {
		pricing.getCommodityCurves().addAll((Collection<? extends CommodityCurve>) commodityIndexImporter.importObjects(PricingPackage.eINSTANCE.getCommodityCurve(), csvReader, context));
	}

	private void importSettledPrices(final PricingModel pricing, final CSVReader csvReader, final IMMXImportContext context) {
		pricing.getSettledPrices().addAll((Collection<? extends DatePointContainer>) settledPricesImporter.importObjects(PricingPackage.eINSTANCE.getDatePointContainer(), csvReader, context));
	}

	private void importCurrencyCurves(final PricingModel pricing, final CSVReader csvReader, final IMMXImportContext context) {
		pricing.getCurrencyCurves().addAll((Collection<? extends CurrencyCurve>) currencyIndexImporter.importObjects(PricingPackage.eINSTANCE.getCurrencyCurve(), csvReader, context));
	}

	private void importCharterCurves(final PricingModel pricing, final CSVReader csvReader, final IMMXImportContext context) {
		pricing.getCharterCurves().addAll((Collection<? extends CharterCurve>) charterIndexImporter.importObjects(PricingPackage.eINSTANCE.getCharterCurve(), csvReader, context));
	}

	private void importBaseFuelCurves(final PricingModel pricing, final CSVReader csvReader, final IMMXImportContext context) {
		pricing.getBunkerFuelCurves().addAll((Collection<? extends BunkerFuelCurve>) baseFuelIndexImporter.importObjects(PricingPackage.eINSTANCE.getBunkerFuelCurve(), csvReader, context));
	}

	private void importConversionFactors(final PricingModel pricing, final CSVReader csvReader, final IMMXImportContext context) {
		pricing.getConversionFactors().addAll((Collection<? extends UnitConversion>) conversionFactorsImporter.importObjects(PricingPackage.eINSTANCE.getUnitConversion(), csvReader, context));
	}

	@Override
	public void exportModel(final EObject model, final Map<String, Collection<Map<String, String>>> output, final IMMXExportContext context) {
		final PricingModel pricing = (PricingModel) model;
		output.put(PRICE_CURVE_KEY, commodityIndexImporter.exportObjects(pricing.getCommodityCurves(), context));
		output.put(CHARTER_CURVE_KEY, charterIndexImporter.exportObjects(pricing.getCharterCurves(), context));
		output.put(BASEFUEL_PRICING_KEY, baseFuelIndexImporter.exportObjects(pricing.getBunkerFuelCurves(), context));
		output.put(CURRENCY_CURVE_KEY, currencyIndexImporter.exportObjects(pricing.getCurrencyCurves(), context));
		output.put(SETTLED_PRICES_KEY, settledPricesImporter.exportObjects(pricing.getSettledPrices(), context));
		output.put(CONVERSION_FACTORS_KEY, conversionFactorsImporter.exportObjects(pricing.getConversionFactors(), context));
	}

	@Override
	public EClass getEClass() {
		return PricingPackage.eINSTANCE.getPricingModel();
	}

	private void makeConversionFactor(final String from, final String to, final double f, final PricingModel pricingModel) {
		final UnitConversion factor = PricingFactory.eINSTANCE.createUnitConversion();
		factor.setFrom(from);
		factor.setTo(to);
		factor.setFactor(f);
		pricingModel.getConversionFactors().add(factor);
	}

}
