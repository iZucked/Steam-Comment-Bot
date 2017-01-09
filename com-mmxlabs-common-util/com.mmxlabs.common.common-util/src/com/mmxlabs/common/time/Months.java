/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZonedDateTime;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.NonNullPair;

/**
 * Returns the rounded down months between two date objects
 * 
 * @author Simon Goodall
 *
 */
public final class Months {

	public static int between(@NonNull final YearMonth start, @NonNull final YearMonth end) {

		final int endMonths = end.getYear() * 12 + end.getMonthValue();
		final int startMonths = start.getYear() * 12 + start.getMonthValue();
		return endMonths - startMonths;
	}

	public static int between(@NonNull final LocalDate start, @NonNull final LocalDate end) {

		// Assume all months are 31 days. This also neatly covers months which are less than 31 days
		final int endDays = (end.getYear() * 12 + end.getMonthValue()) * 31 + end.getDayOfMonth();
		final int startDays = (start.getYear() * 12 + start.getMonthValue()) * 31 + start.getDayOfMonth();
		final int daysDiff = endDays - startDays;
		final int months = Math.floorDiv(Math.abs(daysDiff), 31);
		return daysDiff < 0 ? -months : months;
	}

	public static int in(@NonNull final NonNullPair<ZonedDateTime, ZonedDateTime> interval) {
		return Months.between(interval.getFirst(), interval.getSecond());
	}

	public static int between(@NonNull final ZonedDateTime start, @NonNull final ZonedDateTime end) {

		final int endDays = (end.getYear() * 12 + end.getMonthValue()) * 31 + end.getDayOfMonth();
		final int startDays = (start.getYear() * 12 + start.getMonthValue()) * 31 + start.getDayOfMonth();
		final int daysDiff = endDays - startDays;
		final int months = Math.floorDiv(Math.abs(daysDiff), 31);
		return daysDiff < 0 ? -months : months;
	}

	public static int between(@NonNull final LocalDateTime start, @NonNull final LocalDateTime end) {

		final int endDays = (end.getYear() * 12 + end.getMonthValue()) * 31 + end.getDayOfMonth();
		final int startDays = (start.getYear() * 12 + start.getMonthValue()) * 31 + start.getDayOfMonth();
		final int daysDiff = endDays - startDays;
		final int months = Math.floorDiv(Math.abs(daysDiff), 31);
		return daysDiff < 0 ? -months : months;
	}
}
