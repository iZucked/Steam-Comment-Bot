package org.eclipse.nebula.widgets.ganttchart.label;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.nebula.widgets.ganttchart.GanttEvent;
import org.eclipse.nebula.widgets.ganttchart.label.internal.GeneratedEventText;

@NonNullByDefault
public interface IEventsLabelManager {
	@Nullable
	List<GeneratedEventText> getEventTexts(GanttEvent event, int eventWidth);

	void reset();

	void dispose();
}
