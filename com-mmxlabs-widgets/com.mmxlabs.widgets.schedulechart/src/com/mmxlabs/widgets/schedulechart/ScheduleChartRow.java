/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import java.util.List;

public class ScheduleChartRow {

	private final List<ScheduleEvent> events;
	private final String name;
	
	public ScheduleChartRow(String name, List<ScheduleEvent> events) {
		this.name = name;
		this.events = events;
	}
	
	public List<ScheduleEvent> getEvents() {
		return events;
	}

	public String getName() {
		return name;
	}
}
