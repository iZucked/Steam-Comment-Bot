/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.shiplingo.platform.scheduleview.views.IScheduleViewColourScheme;

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
			if (((Journey) element).getToll() > 0) {
				return ColorCache.getColor(0, 0, 255);
			}
		}

		if (element instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) element;

			if (visit.getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime().before(visit.getStart())) {
				return ColorCache.getColor(255, 0, 0);
			}
		}
		return null;
	}

}
