/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.common.exposures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.common.calendars.BasicHolidayCalendar;
import com.mmxlabs.common.calendars.BasicPricingCalendar;
import com.mmxlabs.common.curves.BasicCommodityCurveData;
import com.mmxlabs.common.curves.BasicUnitConversionData;
import com.mmxlabs.common.parser.nodes.MarkedUpNode;
import com.mmxlabs.common.parser.nodes.Node;

public class ExposuresLookupData {
	public Map<String, BasicCommodityCurveData> commodityMap = new HashMap<>();
	public List<String> currencyList = new ArrayList<>();
	public Map<String, BasicUnitConversionData> conversionMap = new HashMap<>();
	public Map<String, BasicUnitConversionData> reverseConversionMap = new HashMap<>();

	public Map<String, Node> expressionCache = new HashMap<>();
	public Map<String, MarkedUpNode> expressionToNode = new HashMap<>();
	
	// Keeps a Pricing calendar for a curve name
	public Map<String, BasicPricingCalendar> pricingCalendars = new HashMap<>();
	// Keeps a Holiday calendar for a curve name
	public Map<String, BasicHolidayCalendar> holidayCalendars = new HashMap<>();
}