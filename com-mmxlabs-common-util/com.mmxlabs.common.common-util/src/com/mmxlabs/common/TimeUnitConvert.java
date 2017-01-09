/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common;

/**
 * Class for methods that convert measurements that are per one time unit to another unit of time.
 * 
 * @author Adam Semenenko
 * 
 */
public final class TimeUnitConvert {

	private TimeUnitConvert() {
	}

	/**
	 * Convert a unit that is measured per second to per minute (multiply unitPerSecond by 60).
	 * 
	 * @param unitPerSecond
	 *            The measurement per Second to convert to per minute.
	 * @return The measurement per minute.
	 */
	public static int convertPerSecondToPerMinute(final int unitPerSecond) {
		return unitPerSecond * 60;
	}

	/**
	 * Convert a unit that is measured per minute to per hours (multiply unitPerMinute by 60).
	 * 
	 * @param unitPerMinute
	 *            The measurement per minute to convert to per hour.
	 * @return The measurement per hour.
	 */
	public static int convertPerMinuteToPerHour(final int unitPerMinute) {
		return unitPerMinute * 60;
	}

	/**
	 * Convert a unit that is measured per hour to per day (multiply unitPerHour by 24).
	 * 
	 * @param unitPerHour
	 *            The measurement per hour to convert to per day.
	 * @return The measurement per day.
	 */
	public static int convertPerHourToPerDay(final int unitPerHour) {
		return unitPerHour * 24;
	}
}
