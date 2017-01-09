/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.timezone;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Methods to help move between timezones.
 * @author achurchill
 *
 */
public class TimeZoneHelper {
	/**
	 * Helper method to create a new Date, preserving everything but changing the timezone. In effect this will shift the time.
	 * @param originalDate
	 * @param originalTimeZone
	 * @param newTimeZone
	 * @return
	 */
	public static Date createTimeZoneShiftedDate(Date originalDate, String originalTimeZone, String newTimeZone) {
		return createTimeZoneShiftedCalendar(originalDate, originalTimeZone, newTimeZone).getTime();
	}
	
	/**
	 * Helper method to create a new Calendar, preserving everything but changing the timezone. In effect this will shift the time.
	 * @param originalDate
	 * @param originalTimeZone
	 * @param newTimeZone
	 * @return
	 */
	public static Calendar createTimeZoneShiftedCalendar(Date originalDate, String originalTimeZone, String newTimeZone) {
		final Calendar oldCalendar = Calendar.getInstance(TimeZone.getTimeZone(originalTimeZone));
		oldCalendar.setTime(originalDate);
		final Calendar newCalendar = Calendar.getInstance(TimeZone.getTimeZone(newTimeZone));
		newCalendar.set(Calendar.YEAR, oldCalendar.get(Calendar.YEAR));
		newCalendar.set(Calendar.MONTH, oldCalendar.get(Calendar.MONTH));
		newCalendar.set(Calendar.DAY_OF_MONTH, oldCalendar.get(Calendar.DAY_OF_MONTH));
		newCalendar.set(Calendar.HOUR_OF_DAY, oldCalendar.get(Calendar.HOUR_OF_DAY));
		newCalendar.set(Calendar.MINUTE, oldCalendar.get(Calendar.MINUTE));
		newCalendar.set(Calendar.SECOND, 0);
		newCalendar.set(Calendar.MILLISECOND, 0);

		return newCalendar;
	}
	
	/**
	 * Helper method to create a new Calendar, preserving everything but changing the timezone. In effect this will shift the time.
	 * @param originalDate
	 * @param originalTimeZone
	 * @param newTimeZone
	 * @return
	 */
	public static Calendar createTimeZoneShiftedCalendar(Calendar originalDate, String originalTimeZone, String newTimeZone) {
		return createTimeZoneShiftedCalendar(originalDate.getTime(), originalTimeZone, newTimeZone);
	}
}
