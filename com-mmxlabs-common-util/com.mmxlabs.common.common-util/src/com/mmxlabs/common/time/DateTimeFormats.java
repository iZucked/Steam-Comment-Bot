/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.time;

import java.text.SimpleDateFormat;

/**
 * System dependent date time format used.
 * A duplication of DateTimeFormatProvider in models.ui.date
 * 
 * @author Andrey Popov
 *
 */
public class DateTimeFormats {

	private DateTimeFormats() {}
	
	public static final String PREFERRED_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm";
	public static final String PREFERRED_DATE_FORMAT = "yyyy/MM/dd";
	
	private static final String DATE_STRING_DISPLAY;
	private static final String DATE_TIME_STRING_DISPLAY;
	static {
		// Get the system date format and determine if system is month/day or day/month
		final SimpleDateFormat sdf = new SimpleDateFormat();
		final String pattern = sdf.toPattern();
		final int monthIdx = pattern.indexOf('M');
		final int dayIdx = pattern.indexOf('d');
		if (dayIdx < monthIdx) {
			DATE_STRING_DISPLAY = "dd/MM/yy";
			DATE_TIME_STRING_DISPLAY = "dd/MM/yy HH:mm";
		} else {
			DATE_STRING_DISPLAY = "MM/dd/yy";
			DATE_TIME_STRING_DISPLAY = "MM/dd/yy HH:mm";
		}
	}
	
	public static String getDateStringDisplay() {
		return DATE_STRING_DISPLAY;
	}
	public static String getDateTimeStringDisplay() {
		return DATE_TIME_STRING_DISPLAY;
	}
}
