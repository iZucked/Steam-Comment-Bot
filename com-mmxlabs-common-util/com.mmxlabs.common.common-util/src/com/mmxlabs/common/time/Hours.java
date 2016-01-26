/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
 * Returns the rounded down hours between two date objects.
 * 
 * @author Simon Goodall
 *
 */
public class Hours {

	public static int between(@NonNull final YearMonth start, @NonNull final YearMonth end) {

		final ZonedDateTime _start = start.atDay(1).atStartOfDay(ZoneId.of("UTC"));
		final ZonedDateTime _end = end.atDay(1).atStartOfDay(ZoneId.of("UTC"));
		return Hours.between(_start, _end);
	}

	public static int between(@NonNull final LocalDate start, @NonNull final LocalDate end) {
		final Duration duration = Duration.between(start.atStartOfDay(), end.atStartOfDay());
		return (int) duration.toHours();
	}

	// public static int between(@NonNull final Instant start, @NonNull final Instant end) {
	// final Duration duration = Duration.between(start, end);
	// return (int) duration.toHours();
	// }

	public static int in(@NonNull NonNullPair<ZonedDateTime, ZonedDateTime> interval) {
		return Hours.between(interval.getFirst(), interval.getSecond());
	}

	// public static int in(@NonNull final Interval interval) {
	// return Hours.between(interval.getStart(), interval.getEnd());
	// }

	public static int between(@NonNull final ZonedDateTime start, @NonNull final ZonedDateTime end) {
		final Duration duration = Duration.between(start, end);
		return (int) duration.toHours();
	}

	public static int between(@NonNull LocalDateTime start, @NonNull LocalDateTime end) {
		final Duration duration = Duration.between(start, end);
		return (int) duration.toHours();
	}
}
