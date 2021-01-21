/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.time.ZonedDateTime;

import com.mmxlabs.common.Pair;

public class SeriesParserData {

	public ShiftFunctionMapper shiftMapper;
	public CalendarMonthMapper calendarMonthMapper;
	public Pair<ZonedDateTime, ZonedDateTime> earliestAndLatestTime;

	public void setCalendarMonthMapper(CalendarMonthMapper calendarMonthMapper) {
		this.calendarMonthMapper = calendarMonthMapper;

	}

	public void setShiftMapper(ShiftFunctionMapper shiftMapper) {
		this.shiftMapper = shiftMapper;
	}

	public Pair<ZonedDateTime, ZonedDateTime> getEarliestAndLatestTime() {
		return earliestAndLatestTime;
	}

	public void setEarliestAndLatestTime(Pair<ZonedDateTime, ZonedDateTime> earliestAndLatestTime) {
		this.earliestAndLatestTime = earliestAndLatestTime;
	}

	public ShiftFunctionMapper getShiftMapper() {
		return shiftMapper;
	}

	public CalendarMonthMapper getCalendarMonthMapper() {
		return calendarMonthMapper;
	}
}
