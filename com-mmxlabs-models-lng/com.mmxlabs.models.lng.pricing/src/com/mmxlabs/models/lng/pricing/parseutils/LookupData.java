/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.parseutils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.nodes.MarkedUpNode;
import com.mmxlabs.common.parser.nodes.Node;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.BunkerFuelCurve;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.CurrencyCurve;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;

public class LookupData {
	public PricingModel pricingModel;
	public Map<String, CommodityCurve> commodityMap = new HashMap<>();
	public Map<String, CharterCurve> charterMap = new HashMap<>();
	public Map<String, BunkerFuelCurve> baseFuelMap = new HashMap<>();
	public Map<String, CurrencyCurve> currencyMap = new HashMap<>();
	public Map<String, UnitConversion> conversionMap = new HashMap<>();
	public Map<String, UnitConversion> reverseConversionMap = new HashMap<>();

	public Map<String, Node> expressionCache = new HashMap<>();
	public Map<String, Collection<AbstractYearMonthCurve>> expressionCache2 = new HashMap<>();
	public Map<String, MarkedUpNode> expressionToNode = new HashMap<>();

	public static @NonNull LookupData createLookupData(final PricingModel pricingModel) {
		final LookupData lookupData = new LookupData();
		lookupData.pricingModel = pricingModel;

		pricingModel.getCommodityCurves().stream().filter(idx -> idx.getName() != null).forEach(idx -> lookupData.commodityMap.put(idx.getName().toLowerCase(), idx));
		pricingModel.getCurrencyCurves().stream().filter(idx -> idx.getName() != null).forEach(idx -> lookupData.currencyMap.put(idx.getName().toLowerCase(), idx));
		pricingModel.getCharterCurves().stream().filter(idx -> idx.getName() != null).forEach(idx -> lookupData.charterMap.put(idx.getName().toLowerCase(), idx));
		pricingModel.getBunkerFuelCurves().stream().filter(idx -> idx.getName() != null).forEach(idx -> lookupData.baseFuelMap.put(idx.getName().toLowerCase(), idx));
		pricingModel.getConversionFactors().forEach(f -> {
			final String conversionFactorName = PriceIndexUtils.createConversionFactorName(f);
			if (conversionFactorName != null) {
				lookupData.conversionMap.put(conversionFactorName.toLowerCase(), f);
			}
		});
		pricingModel.getConversionFactors().forEach(f -> {
			final String conversionFactorName = PriceIndexUtils.createReverseConversionFactorName(f);
			if (conversionFactorName != null) {
				lookupData.reverseConversionMap.put(conversionFactorName.toLowerCase(), f);
			}
		});

		return lookupData;

	}
}