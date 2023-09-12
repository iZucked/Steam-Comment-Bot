package com.mmxlabs.widgets.schedulechart.providers;

import java.time.LocalDateTime;

public interface IScheduleChartModelUpdater {
	public void annotationEdited(Object scheduleEventData, Object annotationData, LocalDateTime windowStart, LocalDateTime windowEnd);
}
