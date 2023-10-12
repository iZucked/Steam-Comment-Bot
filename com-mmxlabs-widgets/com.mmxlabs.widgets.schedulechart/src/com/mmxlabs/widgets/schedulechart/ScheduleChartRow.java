/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import java.util.List;

public class ScheduleChartRow {

	private final List<ScheduleEvent> events;
	private final ScheduleChartRowKey key;
	private final ScheduleChartRowPriorityType rowType;
	
	public ScheduleChartRow(final ScheduleChartRowKey key, final List<ScheduleEvent> events, final ScheduleChartRowPriorityType rowType) {
		this.key = key;
		this.events = events;
		this.rowType = rowType;
	}
	
	public List<ScheduleEvent> getEvents() {
		return events;
	}
	
	public ScheduleChartRowKey getKey() {
		return key;
	}
	
	public ScheduleChartRowKeyGrouping getKeyGrouping() {
		return key.getGrouping();
	}

	public String getName() {
		return key.getName();
	}

	public ScheduleChartRowPriorityType getRowType() {
		return rowType;
	}
}
