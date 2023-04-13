/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package org.eclipse.nebula.widgets.ganttchart.label;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.nebula.widgets.ganttchart.GanttEvent;

@NonNullByDefault
public interface IEventTextPropertiesGenerator {

	String generateText(final GanttEvent event);

	EEventLabelAlignment getAlignment();
}
