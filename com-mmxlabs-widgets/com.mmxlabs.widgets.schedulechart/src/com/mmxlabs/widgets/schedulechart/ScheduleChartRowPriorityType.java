/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

/**
 * Allows ditinguish Schedule Chart Rows by category
 * @author AP
 *
 */
public enum ScheduleChartRowPriorityType {
	SPECIAL_TOP_ROW(0),
	SPECIAL_SECOND_TOP_ROW(1),
	REGULAR_ROWS(2);

	private final int priority;

	ScheduleChartRowPriorityType(int priority) {
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}
}
