package org.eclipse.nebula.widgets.ganttchart.label;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.nebula.widgets.ganttchart.GanttEvent;

@NonNullByDefault
public interface IFromEventTextGenerator {

	String generateText(final GanttEvent event);
}
