package org.eclipse.nebula.widgets.ganttchart.plaque;

import org.eclipse.nebula.widgets.ganttchart.GanttEvent;

public interface IPlaqueContentProvider {

	String provideContents(GanttEvent event);
}
