/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.common.time;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.NonNullPair;

/**
 * Returns the rounded down days between two date objects.
 * 
 * @author Simon Goodall
 *
 */
public final class Days {

	private Days() {

	}

	public static int between(@NonNull final YearMonth start, @NonNull final YearMonth end) {

		final ZonedDateTime zonedStart = start.atDay(1).atStartOfDay(ZoneId.of("UTC"));
		final ZonedDateTime zonedEnd = end.atDay(1).atStartOfDay(ZoneId.of("UTC"));
		return Days.between(zonedStart, zonedEnd);
	}

	public static int between(@NonNull final LocalDate start, @NonNull final LocalDate end) {
		final Duration duration = Duration.between(start.atStartOfDay(), end.atStartOfDay());
		return (int) duration.toDays();
	}

	public static int in(@NonNull final NonNullPair<ZonedDateTime, ZonedDateTime> interval) {
		return Days.between(interval.getFirst(), interval.getSecond());
	}

	public static int between(@NonNull final ZonedDateTime start, @NonNull final ZonedDateTime end) {
		final Duration duration = Duration.between(start, end);
		return (int) duration.toDays();
	}

	public static int between(@NonNull final LocalDateTime start, @NonNull final LocalDateTime end) {
		final Duration duration = Duration.between(start, end);
		return (int) duration.toDays();
	}
}
