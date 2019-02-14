/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling;

public class IntervalData {
	public final int start; // Inclusive
	public final int end; // Inclusive
	public final int price;
	
	public IntervalData(final int start, final int end, final int price) {
		this.start = start;
		this.end = end;
		this.price = price;
	}
}
