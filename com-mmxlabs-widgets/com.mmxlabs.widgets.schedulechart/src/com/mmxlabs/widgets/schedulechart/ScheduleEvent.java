package com.mmxlabs.widgets.schedulechart;

import java.time.LocalDateTime;

public class ScheduleEvent {
	private final LocalDateTime startDate;
	private final LocalDateTime endDate;
	
	public ScheduleEvent(LocalDateTime startDate, LocalDateTime endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public LocalDateTime getStart() {
		return startDate;
	}
	
	public LocalDateTime getEnd() {
		return endDate;
	}
}
