package com.mmxlabs.widgets.schedulechart;

/**
 * Ability to change some rows' sizes and priorities
 * @author AP
 *
 */
public interface IScheduleChartRowsDataProvider {
	boolean isNoSpacerRow(ScheduleChartRow row);

	ScheduleChartRowPriorityType getPriorityOf(ScheduleChartRowKey key);
}
