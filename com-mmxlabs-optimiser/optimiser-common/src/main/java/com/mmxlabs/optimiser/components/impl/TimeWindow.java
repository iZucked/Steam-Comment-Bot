package com.mmxlabs.optimiser.components.impl;

import com.mmxlabs.optimiser.components.ITimeWindow;

public final class TimeWindow implements ITimeWindow {

	private final int start;

	private final int end;

	public TimeWindow(final int start, final int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public int getEnd() {
		return end;
	}

	@Override
	public int getStart() {
		return start;
	}

	@Override
	public boolean equals(final Object obj) {
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
}
