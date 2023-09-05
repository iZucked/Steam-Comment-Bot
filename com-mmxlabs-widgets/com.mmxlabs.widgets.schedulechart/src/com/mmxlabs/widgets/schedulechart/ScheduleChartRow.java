/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import java.util.List;

public class ScheduleChartRow {

	private final List<ScheduleEvent> events;
	private final ScheduleChartRowKey key;
	
	public ScheduleChartRow(final ScheduleChartRowKey key, final List<ScheduleEvent> events) {
		this.key = key;
		this.events = events;
	}
	
	public List<ScheduleEvent> getEvents() {
		return events;
	}
	
	public ScheduleChartRowKey getKey() {
		return key;
	}

	public String getName() {
		return key.getName();
	}
}
