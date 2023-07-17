package com.mmxlabs.lingo.reports.scheduleview.views;

import com.mmxlabs.ganttviewer.GanttSectionSizeProvider;
import com.mmxlabs.models.lng.schedule.util.PositionsSequence;

/**
 * Class that makes sure that some sections on the GanttChart have fixed size
 * @author Andrey Popov
 *
 */
public class ScheduleChartSizeProvider implements GanttSectionSizeProvider {
	@Override
	public boolean requiresFixedRowHeight(Object resource) {
		return resource instanceof PositionsSequence;
	}
}
