/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.pricing.BunkerFuelCurve;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.CurrencyCurve;
import com.mmxlabs.models.lng.pricing.DatePointContainer;
import com.mmxlabs.models.lng.pricing.MarketIndex;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SettleStrategy;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;
import com.mmxlabs.rcp.common.versions.VersionsUtil;

/**
 */
public class PricingModelImporter implements ISubmodelImporter {

	public static final String PRICE_CURVE_KEY = "PRICE_CURVES";
	public static final String CHARTER_CURVE_KEY = "CHARTER_CURVES";
	public static final String BASEFUEL_PRICING_KEY = "BF_PRICING";
	public static final String CURRENCY_CURVE_KEY = "CURRENCY_CURVES";
	public static final String CONVERSION_FACTORS_KEY = "CONVERSION_FACTORS";
	public static final String SETTLED_PRICES_KEY = "SETTLED_PRICES";
	public static final String HOLIDAY_CALENDAR_KEY = "HOLIDAY_CALENDAR_KEY";
	public static final String PRICING_CALENDAR_KEY = "PRICING_CALENDAR_KEY";
	public static final String SETTLE_STRATEGY_KEY = "SETTLE_STRATEGY_KEY";
	public static final String MARKET_INDEX_KEY = "MARKET_INDEX_KEY";
	public static final String FORMULA_KEY = "FORMULA";

	@Inject
	private IImporterRegistry importerRegistry;

	private IClassImporter commodityIndexImporter;
	private IClassImporter charterIndexImporter;
	private IClassImporter baseFuelIndexImporter;
	private IClassImporter currencyIndexImporter;
	private IClassImporter conversionFactorsImporter;
	private IClassImporter settledPricesImporter;
	private IClassImporter marketIndexImporter;
	private IClassImporter settleStrategyImporter;
	private FormulaImporter formulaImporter;
	private HolidayCalendarImporter holidayCalendarImporter;
	private PricingCalendarImporter pricingCalendarImporter;

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
			formulaImporter = new FormulaImporter();
			charterIndexImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getCharterCurve());
			baseFuelIndexImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getBunkerFuelCurve());
			currencyIndexImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getCurrencyCurve());
			conversionFactorsImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getUnitConversion());
			settledPricesImporter = new DatePointImporter();
			holidayCalendarImporter = new HolidayCalendarImporter();
			pricingCalendarImporter = new PricingCalendarImporter();
			settleStrategyImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getSettleStrategy());
			marketIndexImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getMarketIndex());
		}
	}

	@Override
	public Map<String, String> getRequiredInputs() {
		final HashMap<String, String> inputs = new HashMap<>();
		inputs.put(PRICE_CURVE_KEY, "Commodity Curves");
		inputs.put(CHARTER_CURVE_KEY, "Charter Curves");
		inputs.put(BASEFUEL_PRICING_KEY, "Base Fuel Curves");
		inputs.put(CURRENCY_CURVE_KEY, "Currency Curves");
		inputs.put(FORMULA_KEY, "Formulae");
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_PAPER_DEALS)) {
			inputs.put(SETTLED_PRICES_KEY, "Settled Prices");
			inputs.put(HOLIDAY_CALENDAR_KEY, "Holiday Calendars");
			inputs.put(SETTLE_STRATEGY_KEY, "Settle Strategies");
			inputs.put(MARKET_INDEX_KEY, "Market Indices");
			inputs.put(PRICING_CALENDAR_KEY, "Pricing Calendars");
		}
		inputs.put(CONVERSION_FACTORS_KEY, "Conversion Factors");

		return inputs;
	}

	@Override
	public UUIDObject importModel(final Map<String, CSVReader> inputs, final IMMXImportContext context) {
		final PricingModel pricingModel = PricingFactory.eINSTANCE.createPricingModel();
		if (inputs.containsKey(PRICE_CURVE_KEY)) {
			importCommodityCurves(pricingModel, inputs.get(PRICE_CURVE_KEY), context);
		}
		if (inputs.containsKey(FORMULA_KEY)) {
			importFormulae(pricingModel, inputs.get(FORMULA_KEY), context);
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
		if (inputs.containsKey(HOLIDAY_CALENDAR_KEY)) {
			importIndexHolidays(pricingModel, inputs.get(HOLIDAY_CALENDAR_KEY), context);
		}
		if (inputs.containsKey(PRICING_CALENDAR_KEY)) {
			importPricingCalendars(pricingModel, inputs.get(PRICING_CALENDAR_KEY), context);
		}
		if (inputs.containsKey(CONVERSION_FACTORS_KEY)) {
			importConversionFactors(pricingModel, inputs.get(CONVERSION_FACTORS_KEY), context);
		}
		if (inputs.containsKey(MARKET_INDEX_KEY)) {
			importMarketIndices(pricingModel, inputs.get(MARKET_INDEX_KEY), context);
		}
		if (inputs.containsKey(SETTLE_STRATEGY_KEY)) {
			importSettleStrategies(pricingModel, inputs.get(SETTLE_STRATEGY_KEY), context);
		}
		if (pricingModel.getConversionFactors().isEmpty()) {
			// Create default conversion factors
			makeConversionFactor("therm", "mmBtu", 10, pricingModel);
			makeConversionFactor("bbl", "mmBtu", 0.180136, pricingModel);
			makeConversionFactor("MWh", "mmBtu", 0.293297, pricingModel);
		}

		pricingModel.setMarketCurvesVersionRecord(VersionsUtil.createNewRecord());
		pricingModel.setSettledPricesVersionRecord(VersionsUtil.createNewRecord());

		return pricingModel;
	}

	private void importMarketIndices(PricingModel pricingModel, CSVReader csvReader, @NonNull IMMXImportContext context) {
		pricingModel.getMarketIndices().addAll((Collection<? extends MarketIndex>) marketIndexImporter.importObjects(PricingPackage.eINSTANCE.getMarketIndex(), csvReader, context));
	}

	private void importSettleStrategies(PricingModel pricingModel, CSVReader csvReader, @NonNull IMMXImportContext context) {
		pricingModel.getSettleStrategies().addAll((Collection<? extends SettleStrategy>) settleStrategyImporter.importObjects(PricingPackage.eINSTANCE.getSettleStrategy(), csvReader, context));
	}

	private void importCommodityCurves(final PricingModel pricing, final CSVReader csvReader, final IMMXImportContext context) {
		pricing.getCommodityCurves().addAll((Collection<? extends CommodityCurve>) commodityIndexImporter.importObjects(PricingPackage.eINSTANCE.getCommodityCurve(), csvReader, context));
	}
	
	private void importFormulae(final PricingModel pricing, final CSVReader csvReader, final IMMXImportContext context) {
		pricing.getFormulaeCurves().addAll((Collection<? extends CommodityCurve>) formulaImporter.importObjects(PricingPackage.eINSTANCE.getCommodityCurve(), csvReader, context));
	}

	private void importSettledPrices(final PricingModel pricing, final CSVReader csvReader, final IMMXImportContext context) {
		pricing.getSettledPrices().addAll((Collection<? extends DatePointContainer>) settledPricesImporter.importObjects(PricingPackage.eINSTANCE.getDatePointContainer(), csvReader, context));
	}

	private void importIndexHolidays(final PricingModel pricing, final CSVReader csvReader, final IMMXImportContext context) {
		pricing.getHolidayCalendars().addAll(holidayCalendarImporter.importObjects(csvReader, context));
	}

	private void importPricingCalendars(final PricingModel pricing, final CSVReader csvReader, final IMMXImportContext context) {
		pricing.getPricingCalendars().addAll(pricingCalendarImporter.importObjects(csvReader, context));
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
		output.put(FORMULA_KEY, formulaImporter.exportObjects(pricing.getFormulaeCurves(), context));
		output.put(CHARTER_CURVE_KEY, charterIndexImporter.exportObjects(pricing.getCharterCurves(), context));
		output.put(BASEFUEL_PRICING_KEY, baseFuelIndexImporter.exportObjects(pricing.getBunkerFuelCurves(), context));
		output.put(CURRENCY_CURVE_KEY, currencyIndexImporter.exportObjects(pricing.getCurrencyCurves(), context));
		output.put(SETTLED_PRICES_KEY, settledPricesImporter.exportObjects(pricing.getSettledPrices(), context));
		output.put(CONVERSION_FACTORS_KEY, conversionFactorsImporter.exportObjects(pricing.getConversionFactors(), context));
		output.put(HOLIDAY_CALENDAR_KEY, holidayCalendarImporter.exportObjects(pricing.getHolidayCalendars(), context));
		output.put(PRICING_CALENDAR_KEY, pricingCalendarImporter.exportObjects(pricing.getPricingCalendars(), context));
		output.put(MARKET_INDEX_KEY, marketIndexImporter.exportObjects(pricing.getMarketIndices(), context));
		output.put(SETTLE_STRATEGY_KEY, settleStrategyImporter.exportObjects(pricing.getSettleStrategies(), context));
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
