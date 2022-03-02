/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.calendars;

import java.time.LocalDate;
import java.time.YearMonth;

public class BasicPricingCalendarEntry {
	private YearMonth month;
	private LocalDate start;
	private LocalDate end;
	
	public BasicPricingCalendarEntry(YearMonth month, LocalDate start, LocalDate end) {
		super();
		this.month = month;
		this.start = start;
		this.end = end;
	}
	
	public YearMonth getMonth() {
		return month;
	}
	
	public LocalDate getStart() {
		return start;
	}
	
	public LocalDate getEnd() {
		return end;
	}
}
