/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.components.impl;

import java.util.Objects;

import com.mmxlabs.optimiser.common.components.ITimeWindow;

/**
 * Implementation of {@link ITimeWindow} to create an immutable time window.
 * 
 * @author Simon Goodall
 * 
 */
public final class TimeWindow implements ITimeWindow {

	private final int start;

	private final int end;

	private final int endFlex;

	public TimeWindow(final int start, final int end) {
		this.start = start;
		this.end = end;
		this.endFlex = 0;
	}

	public TimeWindow(final int start, final int end, final int endFlex) {
		this.start = start;
		this.end = end;
		this.endFlex = endFlex;
	}

	@Override
	public final int getExclusiveEnd() {
		return end;
	}

	@Override
	public final int getInclusiveStart() {
		return start;
	}

	@Override
	public int getExclusiveEndFlex() {
		return endFlex;
	}

	@Override
	public int getExclusiveEndWithoutFlex() {
		return end - endFlex;
	}

	@Override
	public final boolean equals(final Object obj) {
		if (obj instanceof TimeWindow) {
			final TimeWindow tw = (TimeWindow) obj;
			if (start != tw.start) {
				return false;
			}
			if (end != tw.end) {
				return false;
			}
			if (endFlex != tw.endFlex) {
				return false;
			}
			return true;
		}

		return false;
	}

	@Override
	public final int hashCode() {
		// Based on Arrays.hashCode(int[])
		return Objects.hash(start, end, endFlex);
	}

	@Override
	public String toString() {
		return String.format("[%d, %d)", start, end);
	}
}
