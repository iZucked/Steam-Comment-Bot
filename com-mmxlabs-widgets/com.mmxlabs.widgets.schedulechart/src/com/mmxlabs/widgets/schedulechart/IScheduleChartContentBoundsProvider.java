package com.mmxlabs.widgets.schedulechart;

import com.mmxlabs.common.Pair;

public interface IScheduleChartContentBoundsProvider {
	/**
	 * @return A pair of the left-most x value and right-most x value where content exists.
	 */
	Pair<Integer, Integer> getContentBounds();
	
	ScheduleEvent getLeftmostEvent();
	
	ScheduleEvent getRightmostEvent();
}
