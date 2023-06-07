package org.eclipse.nebula.widgets.ganttchart.label;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.nebula.widgets.ganttchart.GanttEvent;
import org.eclipse.nebula.widgets.ganttchart.label.internal.GeneratedEventText;

public class NullEventsLabelManager implements IEventsLabelManager {

	@Override
	public @Nullable List<@NonNull GeneratedEventText> getEventTexts(@NonNull GanttEvent event, int eventWidth) {
		return null;
	}

	@Override
	public void reset() {
	}

	@Override
	public void dispose() {
	}

}
