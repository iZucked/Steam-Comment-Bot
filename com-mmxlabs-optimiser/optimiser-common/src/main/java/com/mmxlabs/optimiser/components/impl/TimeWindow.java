package com.mmxlabs.optimiser.components.impl;

import com.mmxlabs.optimiser.components.ITimeWindow;

public final class TimeWindow implements ITimeWindow {

	private final int start;

	private final int end;

	public TimeWindow(int start, int end) {
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
}
