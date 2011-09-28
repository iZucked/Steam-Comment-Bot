/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.common;

/**
 * Class for methods that convert measurements that are per one time unit to another unit of time.
 * 
 * @author Adam Semenenko
 *
 */
public class TimeUnitConvert {


	/**
	 * Convert a unit that is measured per hour to per day (multiply unitPerHour by 24).
	 * 
	 * @param unitPerHour
	 *            The measurement per hour to convert to per day.
	 * @return The measurement per day.
	 */
	public static int convertPerHourToPerDay(int unitPerHour) {
		return unitPerHour * 24;
	}
}
