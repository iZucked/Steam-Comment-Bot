/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.util.Comparator;

import com.mmxlabs.widgets.schedulechart.ScheduleChartRow;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleChartSortingProvider;

public class NinetyDayScheduleChartSortingProvider implements IScheduleChartSortingProvider {

	@Override
	public Comparator<ScheduleChartRow> getComparator() {
		return (o1, o2) -> o1.getName().compareTo(o2.getName());
	}

}
