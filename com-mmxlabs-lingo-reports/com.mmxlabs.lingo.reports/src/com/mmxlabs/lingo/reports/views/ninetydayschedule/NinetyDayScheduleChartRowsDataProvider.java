/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import com.mmxlabs.models.lng.schedule.util.PositionsSequence;
import com.mmxlabs.widgets.schedulechart.IScheduleChartRowsDataProvider;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRow;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRowKey;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRowPriorityType;

public class NinetyDayScheduleChartRowsDataProvider implements IScheduleChartRowsDataProvider {

	@Override
	public boolean isNoSpacerRow(final ScheduleChartRow row) {
		return row.getRowType() == ScheduleChartRowPriorityType.SPECIAL_TOP_ROW || row.getRowType() == ScheduleChartRowPriorityType.SPECIAL_SECOND_TOP_ROW;
	}

	@Override
	public ScheduleChartRowPriorityType getPriorityOf(ScheduleChartRowKey key) {
		if(key.getData() instanceof PositionsSequence) {
			return ScheduleChartRowPriorityType.SPECIAL_TOP_ROW;
		} else {
			return switch (key.getName()) {
			case NinetyDayScheduleChartRowKeys.BUY_ROW -> ScheduleChartRowPriorityType.SPECIAL_TOP_ROW;
			case NinetyDayScheduleChartRowKeys.SELL_ROW -> ScheduleChartRowPriorityType.SPECIAL_SECOND_TOP_ROW;
			default -> ScheduleChartRowPriorityType.REGULAR_ROWS;
			};
		}
	}

}
