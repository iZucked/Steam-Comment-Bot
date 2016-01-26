/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.time;

import java.time.ZonedDateTime;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.NonNullPair;

public class TimeUtils {
	public static boolean overlaps(@Nullable final NonNullPair<ZonedDateTime, ZonedDateTime> a, @Nullable final NonNullPair<ZonedDateTime, ZonedDateTime> b) {
		if (a == null || b == null) {
			return false;
		}

		return within(a, b.getFirst()) ||
//				within(a, b.getSecond()) || 
				within(b, a.getFirst());// || within(b, a.getSecond());
	}

	private static boolean within(final NonNullPair<ZonedDateTime, ZonedDateTime> p, @NonNull ZonedDateTime date) {
		return (!date.isBefore(p.getFirst()) && date.isBefore(p.getSecond()));
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
