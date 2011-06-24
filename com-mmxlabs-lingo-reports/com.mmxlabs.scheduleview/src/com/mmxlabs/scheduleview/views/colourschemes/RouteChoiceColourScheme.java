/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduleview.views.colourschemes;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import scenario.schedule.events.Journey;
import scenario.schedule.events.SlotVisit;

public class RouteChoiceColourScheme implements IScheduleViewColourScheme {

	@Override
	public String getName() {
		return "Route Choice";
	}

	@Override
	public Color getForeground(final Object element) {
		return null;
	}

	@Override
	public Color getBackground(final Object element) {
		if (element instanceof Journey) {
			if (((Journey) element).getRouteCost() > 0) {
				return ColorCache.getColor(0, 0, 255);
			}
		}

		if (element instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) element;

			if (visit.getSlot().getWindowEnd().before(visit.getStartTime())) {
				return ColorCache.getColor(255, 0, 0);
			}
		}
		return null;
	}

}
