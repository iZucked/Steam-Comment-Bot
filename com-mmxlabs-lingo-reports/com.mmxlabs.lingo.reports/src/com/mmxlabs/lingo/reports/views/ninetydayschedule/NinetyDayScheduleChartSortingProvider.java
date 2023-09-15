/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.util.Comparator;

import com.mmxlabs.widgets.schedulechart.ScheduleChartRow;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleChartSortingProvider;

public class NinetyDayScheduleChartSortingProvider implements IScheduleChartSortingProvider {

	/**
	 * Compares chart rows to decide which row should come first on the chart.
	 * 
	 * @implNote it might be a good idea to compare by scenario name first
	 * in case of a pin diff mode. Otherwise rows will not be grouped by scenario. not implemented yet.
	 */
	@Override
	public Comparator<ScheduleChartRow> getComparator() {
		return (firstRow, secondRow) -> {
			
			final int priorityComparison = 
					Integer.compare(firstRow.getRowType().getPriority(), secondRow.getRowType().getPriority());
			
			if (priorityComparison != 0) {
				return priorityComparison;
			}
			
			return firstRow.getName().compareTo(secondRow.getName());
		};
	}

}
