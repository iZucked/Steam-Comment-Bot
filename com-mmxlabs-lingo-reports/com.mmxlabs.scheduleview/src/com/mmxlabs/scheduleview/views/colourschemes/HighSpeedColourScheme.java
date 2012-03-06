/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduleview.views.colourschemes;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class HighSpeedColourScheme implements IScheduleViewColourScheme {

	private final double speed;

	public HighSpeedColourScheme() {
		this(19.0);
	}

	public HighSpeedColourScheme(final double speed) {
		this.speed = speed;
	}

	@Override
	public String getName() {
		return "High Speed";
	}

	@Override
	public Color getForeground(final Object element) {
		return null;
	}

	@Override
	public Color getBackground(final Object element) {
		if (element instanceof Journey) {
			final Journey journey = (Journey) element;
			if (journey.getSpeed() >= speed) {
				return ColorCache.getColor(255, 0, 0);
			} else {
				return ColorCache.getColor(0, 255, 0);
			}
		} else if (element instanceof VesselEventVisit) {
			return ColorCache.getColor(223, 115, 255);
		}

		// else if (mode == Mode.Lateness) {
		if (element instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) element;

			if (visit.getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime().before(visit.getStart())) {
				return ColorCache.getColor(255, 0, 0);
			}
		}
		return null;
	}

}
