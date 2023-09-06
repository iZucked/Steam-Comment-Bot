package com.mmxlabs.widgets.schedulechart.providers;

import java.time.LocalDateTime;

public interface IScheduleChartModelUpdater {
	public void resizeEvent(Object scheduleEventData, LocalDateTime windowStart, LocalDateTime windowEnd);
}
