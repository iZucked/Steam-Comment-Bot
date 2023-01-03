/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.time;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.NonNullPair;

/**
 * Returns the rounded down hours between two date objects.
 * 
 * @author Simon Goodall
 *
 */
@NonNullByDefault
public final class Hours {

	private Hours() {

	}

	public static int between(final YearMonth start, final YearMonth end) {

		final ZonedDateTime lStart = start.atDay(1).atStartOfDay(ZoneId.of("UTC"));
		final ZonedDateTime lEnd = end.atDay(1).atStartOfDay(ZoneId.of("UTC"));
		return Hours.between(lStart, lEnd);
	}

	public static int between(final LocalDate start, final LocalDate end) {
		final Duration duration = Duration.between(start.atStartOfDay(), end.atStartOfDay());
		return (int) duration.toHours();
	}

	public static int in(NonNullPair<ZonedDateTime, ZonedDateTime> interval) {
		return Hours.between(interval.getFirst(), interval.getSecond());
	}

	public static int between(final ZonedDateTime start, final ZonedDateTime end) {
		final Duration duration = Duration.between(start, end);
		return (int) duration.toHours();
	}

	public static int between(LocalDateTime start, LocalDateTime end) {
		final Duration duration = Duration.between(start, end);
		return (int) duration.toHours();
	}
}
