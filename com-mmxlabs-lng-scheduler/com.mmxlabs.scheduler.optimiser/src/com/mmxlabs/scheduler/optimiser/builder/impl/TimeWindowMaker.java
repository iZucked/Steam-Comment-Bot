/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.builder.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.MutableTimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;

public final class TimeWindowMaker {

	@NonNull
	public static ITimeWindow createInclusiveExclusive(final int inclusiveStart, final int exclusiveEnd) {
		return createInclusiveExclusive(inclusiveStart, exclusiveEnd, 0, false);
	}

	@NonNull
	public static ITimeWindow createInclusiveInclusive(final int inclusiveStart, final int inclusiveEnd) {
		return createInclusiveInclusive(inclusiveStart, inclusiveEnd, false);
	}

	@NonNull
	public static ITimeWindow createInclusiveExclusive(final int inclusiveStart, final int exclusiveEnd, boolean mutable) {
		return createInclusiveExclusive(inclusiveStart, exclusiveEnd, 0, mutable);
	}

	@NonNull
	public static ITimeWindow createInclusiveInclusive(final int inclusiveStart, final int inclusiveEnd, boolean mutable) {
		return createInclusiveInclusive(inclusiveStart, inclusiveEnd, 0, mutable);
	}

	@NonNull
	public static ITimeWindow createInclusiveInclusive(final int inclusiveStart, final int inclusiveEnd, final int endFlex, boolean mutable) {
		return createInclusiveExclusive(inclusiveStart, inclusiveEnd + 1, endFlex, mutable);
	}

	@NonNull
	public static ITimeWindow createInclusiveExclusive(final int inclusiveStart, final int exclusiveEnd, final int endFlex, boolean mutable) {

		if (exclusiveEnd != Integer.MIN_VALUE && !(inclusiveStart < exclusiveEnd)) {
			throw new IllegalArgumentException("Start time is greater than end time!");
		}

		final ITimeWindow window;
		if (mutable) {
			window = new MutableTimeWindow(inclusiveStart, exclusiveEnd, endFlex);
		} else {
			window = new TimeWindow(inclusiveStart, exclusiveEnd, endFlex);
		}

		return window;
	}
}
