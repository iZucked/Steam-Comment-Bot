/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.time.LocalDate;

import com.mmxlabs.models.lng.types.TimePeriod;

public class NominationUtils {
	/**
	 * Input the negative window size!
	 * @param localDate
	 * @param timePeriod
	 * @param windowSize
	 * @return
	 */
	public static LocalDate computeNewDate(final LocalDate localDate, final TimePeriod timePeriod, final int windowSize) {
		LocalDate rld = localDate;
		if (timePeriod==TimePeriod.MONTHS) {
			rld = localDate.plusMonths(windowSize);
		} else if(timePeriod==TimePeriod.DAYS) {
			rld = localDate.plusDays(windowSize);
		} else if(timePeriod==TimePeriod.HOURS) {
			rld = localDate.plusDays((long)windowSize/24L);
		}
		return rld;
	}
}
