/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.calendars;

import java.util.ArrayList;
import java.util.List;

public class BasicPricingCalendar {
	
	private final String name;
	private List<BasicPricingCalendarEntry> entries;
	
	public BasicPricingCalendar(final String name) {
		this.name = name;
		this.entries = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public List<BasicPricingCalendarEntry> getEntries() {
		return entries;
	}
}
