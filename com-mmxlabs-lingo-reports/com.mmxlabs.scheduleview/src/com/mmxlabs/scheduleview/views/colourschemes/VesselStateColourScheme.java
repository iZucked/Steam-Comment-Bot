/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduleview.views.colourschemes;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import scenario.schedule.events.Journey;
import scenario.schedule.events.SlotVisit;
import scenario.schedule.events.VesselEventVisit;

public class VesselStateColourScheme implements IScheduleViewColourScheme {

	@Override
	public String getName() {
		return "Vessel State";
	}

	@Override
	public Color getForeground(final Object element) {
		return null;
	}

	@Override
	public Color getBackground(final Object element) {
		if (element instanceof Journey) {
			final Journey journey = (Journey) element;
			if (journey.getVesselState().equals(
					scenario.fleet.VesselState.LADEN)) {
				return ColorCache.getColor(0, 255, 0);
			} else {
				return ColorCache.getColor(0, 0, 255);
			}
		} else if (element instanceof VesselEventVisit) {
			return ColorCache.getColor(223, 115, 255);
		}

		// else if (mode == Mode.Lateness) {
		if (element instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) element;

			if (visit.getSlot().getWindowEnd().before(visit.getStartTime())) {
				return ColorCache.getColor(255, 0, 0);
			}
		}
		return null;
	}

}
