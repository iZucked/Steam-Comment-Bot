/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.common.calendars;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author FM
 *
 */
public class BasicHolidayCalendar {
	private final String name;
	private List<LocalDate> holidays;
	
	public BasicHolidayCalendar(String name) {
		super();
		this.name = name;
		this.holidays = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public List<LocalDate> getHolidays() {
		return holidays;
	}
}
