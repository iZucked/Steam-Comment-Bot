/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.common.time;

import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.function.BiPredicate;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.NonNullPair;

public class TimeUtils {
	public static boolean overlaps(@Nullable final NonNullPair<ZonedDateTime, ZonedDateTime> a, @Nullable final NonNullPair<ZonedDateTime, ZonedDateTime> b) {
		return overlaps(a, b, ZonedDateTime::isBefore);
	}

	public static <T extends Temporal> boolean overlaps(@Nullable final NonNullPair<T, T> a, @Nullable final NonNullPair<T, T> b, BiPredicate<T, T> beforeFunction) {
		if (a == null || b == null) {
			return false;
		}

		return within(a, b.getFirst(), beforeFunction) ||
		// within(a, b.getSecond()) ||
				within(b, a.getFirst(), beforeFunction);// || within(b, a.getSecond());
	}

	public static <T extends Temporal> boolean within(final NonNullPair<T, T> p, @NonNull T date, BiPredicate<T, T> beforeFunction) {
		return (!beforeFunction.test(date, p.getFirst()) && beforeFunction.test(date, p.getSecond()));
	}

	@NonNull
	public static NonNullPair<ZonedDateTime, ZonedDateTime> intersect(@NonNull final NonNullPair<ZonedDateTime, ZonedDateTime> a, @NonNull final NonNullPair<ZonedDateTime, ZonedDateTime> b) {
		final ZonedDateTime s = latest(a.getFirst(), b.getFirst());
		final ZonedDateTime e = earliest(a.getSecond(), b.getSecond());
		return new NonNullPair<>(s, e);
	}

	@NonNull
	public static ZonedDateTime earliest(@NonNull final ZonedDateTime a, @NonNull final ZonedDateTime b) {
		if (a.isBefore(b)) {
			return a;
		}
		return b;
	}

	@NonNull
	public static ZonedDateTime latest(@NonNull final ZonedDateTime a, @NonNull final ZonedDateTime b) {
		if (a.isAfter(b)) {
			return a;
		}
		return b;
	}
}
