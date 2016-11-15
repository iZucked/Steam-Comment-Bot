/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.parseutils;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.CurrencyIndex;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.pricing.parser.Node;

public class LookupData {
	public PricingModel pricingModel;
	public 	Map<String, CommodityIndex> commodityMap = new HashMap<>();
	public Map<String, CurrencyIndex> currencyMap = new HashMap<>();
	public Map<String, UnitConversion> conversionMap = new HashMap<>();
	public Map<String, UnitConversion> reverseConversionMap = new HashMap<>();

	public Map<String, Node> expressionCache = new HashMap<>();
	public Map<String, MarkedUpNode> expressionCache2 = new HashMap<>();
}