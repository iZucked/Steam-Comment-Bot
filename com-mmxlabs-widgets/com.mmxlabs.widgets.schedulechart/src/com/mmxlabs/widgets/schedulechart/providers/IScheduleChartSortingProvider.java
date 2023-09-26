/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.providers;

import java.util.Comparator;

import com.mmxlabs.widgets.schedulechart.ScheduleChartRow;

public interface IScheduleChartSortingProvider {
	
	Comparator<ScheduleChartRow> getComparator();

}
