package com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling;

public class IntervalData {
	public final int start;
	public final int end;
	public final int price;
	
	public IntervalData(final int start, final int end, final int price) {
		this.start = start;
		this.end = end;
		this.price = price;
	}
}
