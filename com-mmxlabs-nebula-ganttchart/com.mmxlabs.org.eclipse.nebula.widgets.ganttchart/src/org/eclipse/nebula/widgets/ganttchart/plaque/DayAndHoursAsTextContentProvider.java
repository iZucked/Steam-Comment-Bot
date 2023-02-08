/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package org.eclipse.nebula.widgets.ganttchart.plaque;

import org.eclipse.nebula.widgets.ganttchart.DateHelper;
import org.eclipse.nebula.widgets.ganttchart.GanttEvent;

public class DayAndHoursAsTextContentProvider implements IPlaqueContentProvider {

	@Override
	public String provideContents(GanttEvent event) {
		final long hours = DateHelper.hoursBetween(event.getActualStartDate(), event.getActualEndDate(), false);
		if (hours == 0L) {
			return "";
		}
		final long integerDivDays = hours / 24L;
		final long remainingHours = hours % 24L;
		if (integerDivDays == 0L) {
			return String.format("%dh", remainingHours);
		} else if (remainingHours == 0L) {
			return String.format("%dd", integerDivDays);
		} else {
			return String.format("%dd %dh", integerDivDays, remainingHours);
		}
	}

}
