/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZonedDateTime;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.NonNullPair;

/**
 * Returns the rounded down months between two date objects
 * 
 * @author Simon Goodall
 *
 */
@NonNullByDefault
public final class Months {

	private Months() {

	}

	/**
	 * Equivalent to {@link #between(YearMonth, YearMonth)} by ignoring day of
	 * month.
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static int between(final LocalDate start, final YearMonth end) {

		final int endMonths = end.getYear() * 12 + end.getMonthValue();
		final int startMonths = start.getYear() * 12 + start.getMonthValue();
		return endMonths - startMonths;
	}

	public static int between(final YearMonth start, final YearMonth end) {

		final int endMonths = end.getYear() * 12 + end.getMonthValue();
		final int startMonths = start.getYear() * 12 + start.getMonthValue();
		return endMonths - startMonths;
	}

	public static int between(final LocalDate start, final LocalDate end) {

		// Assume all months are 31 days. This also neatly covers months which are less
		// than 31 days
		final int endDays = (end.getYear() * 12 + end.getMonthValue()) * 31 + end.getDayOfMonth();
		final int startDays = (start.getYear() * 12 + start.getMonthValue()) * 31 + start.getDayOfMonth();
		final int daysDiff = endDays - startDays;
		final int months = Math.floorDiv(Math.abs(daysDiff), 31);
		return daysDiff < 0 ? -months : months;
	}

	public static int in(final NonNullPair<ZonedDateTime, ZonedDateTime> interval) {
		return Months.between(interval.getFirst(), interval.getSecond());
	}

	public static int between(final ZonedDateTime start, final ZonedDateTime end) {

		final int endDays = (end.getYear() * 12 + end.getMonthValue()) * 31 + end.getDayOfMonth();
		final int startDays = (start.getYear() * 12 + start.getMonthValue()) * 31 + start.getDayOfMonth();
		final int daysDiff = endDays - startDays;
		final int months = Math.floorDiv(Math.abs(daysDiff), 31);
		return daysDiff < 0 ? -months : months;
	}

	public static int between(final LocalDateTime start, final LocalDateTime end) {

		final int endDays = (end.getYear() * 12 + end.getMonthValue()) * 31 + end.getDayOfMonth();
		final int startDays = (start.getYear() * 12 + start.getMonthValue()) * 31 + start.getDayOfMonth();
		final int daysDiff = endDays - startDays;
		final int months = Math.floorDiv(Math.abs(daysDiff), 31);
		return daysDiff < 0 ? -months : months;
	}
}
