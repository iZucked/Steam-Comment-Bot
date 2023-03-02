/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package org.eclipse.nebula.widgets.ganttchart.plaque;

import org.eclipse.nebula.widgets.ganttchart.GanttEvent;

public interface IPlaqueContentProvider {

	String provideContents(GanttEvent event);
}
