package com.mmxlabs.lingo.reports.scheduleview.rendering;

import java.util.Comparator;

import org.eclipse.nebula.widgets.ganttchart.GanttEvent;

import com.mmxlabs.models.lng.schedule.CanalJourneyEvent;
import com.mmxlabs.models.lng.schedule.SlotVisit;

public class DefaultRenderOrderComparator implements Comparator<GanttEvent> {

	@Override
	public int compare(final GanttEvent o1, final GanttEvent o2) {
		// slot visits should be rendered last so the border looks correct
		if (o1.getData() instanceof SlotVisit && o2.getData() instanceof SlotVisit) {
			return 0;
		} else if (o1.getData() instanceof SlotVisit && !(o2.getData() instanceof SlotVisit)) {
			return 1;
		} else if (!(o1.getData() instanceof SlotVisit) && o2.getData() instanceof SlotVisit) {
			return -1;
		// delay CanalJourneyEvent rendering
		} else if (o1.getData() instanceof CanalJourneyEvent && o2.getData() instanceof CanalJourneyEvent) {
			return 0;
		} else if (o1.getData() instanceof CanalJourneyEvent && !(o2.getData() instanceof CanalJourneyEvent)) {
			return 1;
		} else if (!(o1.getData() instanceof CanalJourneyEvent) && o2.getData() instanceof CanalJourneyEvent) {
			return -1;
		}
		// Indifferent to how remainder is ordered
		return 0;
	}

}
