/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.shiplingo.platform.scheduleview.views.IScheduleViewColourScheme;

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
			if (journey.isLaden()) {
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
			if (visit.getStart().after(visit.getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime())) {
				return ColorCache.getColor(255, 0, 0);
			}
			return ColorCache.getColor(0, 0, 0);
		} else if (element instanceof VesselEventVisit) {
			final VesselEventVisit vev = (VesselEventVisit) element;
			if (vev.getStart().after(vev.getVesselEvent().getStartBy())) {
				return ColorCache.getColor(255, 0, 0);
			}
		}
		return null;
	}

}
