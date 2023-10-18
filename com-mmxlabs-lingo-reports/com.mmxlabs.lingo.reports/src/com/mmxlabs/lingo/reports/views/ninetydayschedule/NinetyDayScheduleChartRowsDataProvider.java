/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import com.mmxlabs.widgets.schedulechart.IScheduleChartRowsDataProvider;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRow;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRowKey;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRowPriorityType;

public class NinetyDayScheduleChartRowsDataProvider implements IScheduleChartRowsDataProvider {

	@Override
	public boolean isNoSpacerRow(final ScheduleChartRow row) {
		final String rowName = row.getKey().getName();
		return rowName.equals(NinetyDayScheduleChartRowKeys.BUY_ROW) || rowName.equals(NinetyDayScheduleChartRowKeys.SELL_ROW);
	}

	@Override
	public ScheduleChartRowPriorityType getPriorityOf(ScheduleChartRowKey key) {
		return switch (key.getName()) {
		case NinetyDayScheduleChartRowKeys.BUY_ROW -> ScheduleChartRowPriorityType.SPECIAL_TOP_ROW;
		case NinetyDayScheduleChartRowKeys.SELL_ROW -> ScheduleChartRowPriorityType.SPECIAL_SECOND_TOP_ROW;
		default -> ScheduleChartRowPriorityType.REGULAR_ROWS;
		};
	}

}
