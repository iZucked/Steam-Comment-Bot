/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.parseutils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.CurrencyIndex;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.pricing.parser.Node;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;

public class LookupData {
	public PricingModel pricingModel;
	public Map<String, CommodityIndex> commodityMap = new HashMap<>();
	public Map<String, CharterIndex> charterMap = new HashMap<>();
	public Map<String, BaseFuelIndex> baseFuelMap = new HashMap<>();
	public Map<String, CurrencyIndex> currencyMap = new HashMap<>();
	public Map<String, UnitConversion> conversionMap = new HashMap<>();
	public Map<String, UnitConversion> reverseConversionMap = new HashMap<>();

	public Map<String, Node> expressionCache = new HashMap<>();
	public Map<String, Collection<NamedIndexContainer<?>>> expressionCache2 = new HashMap<>();
	public Map<String, MarkedUpNode> expressionToNode = new HashMap<>();

	public static @NonNull LookupData createLookupData(final PricingModel pricingModel) {
		final LookupData lookupData = new LookupData();
		lookupData.pricingModel = pricingModel;

		pricingModel.getCommodityIndices().stream().filter(idx -> idx.getName() != null).forEach(idx -> lookupData.commodityMap.put(idx.getName().toLowerCase(), idx));
		pricingModel.getCurrencyIndices().stream().filter(idx -> idx.getName() != null).forEach(idx -> lookupData.currencyMap.put(idx.getName().toLowerCase(), idx));
		pricingModel.getCharterIndices().stream().filter(idx -> idx.getName() != null).forEach(idx -> lookupData.charterMap.put(idx.getName().toLowerCase(), idx));
		pricingModel.getBaseFuelPrices().stream().filter(idx -> idx.getName() != null).forEach(idx -> lookupData.baseFuelMap.put(idx.getName().toLowerCase(), idx));
		pricingModel.getConversionFactors().forEach(f -> lookupData.conversionMap.put(PriceIndexUtils.createConversionFactorName(f).toLowerCase(), f));
		pricingModel.getConversionFactors().forEach(f -> lookupData.reverseConversionMap.put(PriceIndexUtils.createReverseConversionFactorName(f).toLowerCase(), f));

		return lookupData;

	}
}