package com.mmxlabs.lingo.reports.scheduleview.rendering;

import java.util.Comparator;

import org.eclipse.nebula.widgets.ganttchart.GanttEvent;

import com.mmxlabs.models.lng.schedule.SlotVisit;

public class DefaultRenderOrderComparator implements Comparator<GanttEvent> {

	@Override
	public int compare(final GanttEvent o1, final GanttEvent o2) {
		// slot visits should be rendered last so the border looks correct
		if (o1.getData() instanceof SlotVisit && !(o2.getData() instanceof SlotVisit)) {
			return 1;
		}
		return -1;
	}

}
