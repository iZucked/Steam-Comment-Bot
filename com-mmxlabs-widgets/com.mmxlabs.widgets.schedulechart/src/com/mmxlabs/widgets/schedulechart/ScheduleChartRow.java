package com.mmxlabs.widgets.schedulechart;

import java.util.List;

public class ScheduleChartRow {
	private final List<ScheduleEvent> events;
	
	public ScheduleChartRow(List<ScheduleEvent> events) {
		this.events = events;
	}
	
	public List<ScheduleEvent> getEvents() {
		return events;
	}
}
