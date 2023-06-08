/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package org.eclipse.nebula.widgets.ganttchart.label;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.nebula.widgets.ganttchart.GanttEvent;
import org.eclipse.nebula.widgets.ganttchart.label.internal.GeneratedEventText;

@NonNullByDefault
public interface IEventsLabelCalculator {
	boolean fitsInWidth(GanttEvent event, List<GeneratedEventText> generatedEventTexts, int eventWidth);

	int calculateMinimumWidth(GanttEvent event, List<GeneratedEventText> generatedEventTexts);
}
