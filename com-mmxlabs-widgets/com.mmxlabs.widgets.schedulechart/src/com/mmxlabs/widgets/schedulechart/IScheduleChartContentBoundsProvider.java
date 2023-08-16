package com.mmxlabs.widgets.schedulechart;

import com.mmxlabs.common.Pair;

public interface IScheduleChartContentBoundsProvider {

	ScheduleEvent getLeftmostEvent();
	
	ScheduleEvent getRightmostEvent();

	/**
	 * @return A pair of the left-most x value and right-most x value where content exists.
	 */
	default Pair<Integer, Integer> getContentBounds(ScheduleTimeScale timeScale) {
		 int min = timeScale.getXBoundsFromEvent(getLeftmostEvent()).getFirst();
		 int max = timeScale.getXBoundsFromEvent(getRightmostEvent()).getSecond();
		 return Pair.of(min, max);
	}
	
}
