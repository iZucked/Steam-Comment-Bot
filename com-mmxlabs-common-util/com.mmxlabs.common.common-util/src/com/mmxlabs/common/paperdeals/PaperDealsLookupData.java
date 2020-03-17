/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.common.paperdeals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.common.calendars.BasicHolidayCalendar;
import com.mmxlabs.common.calendars.BasicPricingCalendar;

/**
 * Class to store pricing and holiday calendar data
 * @author FM
 *
 */
public class PaperDealsLookupData {
	// Keeps a Pricing calendar for a curve name
	public Map<String, BasicPricingCalendar> pricingCalendars = new HashMap<>();
	// Keeps a Holiday calendar for a curve name
	public Map<String, BasicHolidayCalendar> holidayCalendars = new HashMap<>();
	// Keeps collection of settled prices for a curve name
	public Map<String, Map<LocalDate, Double>> settledPrices = new HashMap<>();
	// Keeps a list of paper deals
	public List<BasicPaperDealData> paperDeals = new ArrayList<>();
	// Keeps a map of a buy/sell curve name for a given curve
	public Map<String, Map<String, String>> hedgeCurves = new HashMap<>();
	// Keeps a map of market index for a given curve name
	public Map<String, String> marketIndices = new HashMap<>();
	
	public PaperDealsLookupData(final Map<String, BasicPricingCalendar> pricingCalendars, final Map<String, BasicHolidayCalendar> holidayCalendars, final Map<String, Map<LocalDate, Double>> settledPrices, //
			final List<BasicPaperDealData> paperDeals, final Map<String, Map<String, String>> hedgeCurves, final Map<String, String> marketIndices) {
		this.pricingCalendars = pricingCalendars;
		this.holidayCalendars = holidayCalendars;
		this.settledPrices = settledPrices;
		this.paperDeals = paperDeals;
		this.hedgeCurves = hedgeCurves;
		this.marketIndices = marketIndices;
	}
}
