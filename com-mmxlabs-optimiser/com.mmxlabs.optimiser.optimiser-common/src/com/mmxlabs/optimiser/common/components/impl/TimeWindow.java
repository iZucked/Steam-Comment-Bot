/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.components.impl;

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

	public TimeWindow(final int start, final int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public final int getEnd() {
		return end;
	}

	@Override
	public final int getStart() {
		return start;
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
			return true;
		}

		return false;
	}

	@Override
	public final int hashCode() {
		// Based on Arrays.hashCode(int[])
		return (31 * start) + end;
	}
	
	@Override
	public String toString() {
		return String.format("[%d, %d)", start, end);
	}
}
