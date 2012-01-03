/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduleview.views.colourschemes;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import scenario.schedule.events.FuelPurpose;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.Journey;
import scenario.schedule.events.SlotVisit;

public class FuelChoiceColourScheme implements IScheduleViewColourScheme {

	@Override
	public String getName() {
		return "Fuel Choice";
	}

	@Override
	public Color getForeground(final Object element) {
		return null;
	}

	@Override
	public Color getBackground(final Object element) {
		if (element instanceof Journey) {
			final Journey journey = (Journey) element;

			int r = 0;
			int g = 0;
			int b = 0;

			for (final FuelQuantity fq : journey.getFuelUsage()) {
				if (fq.getQuantity() > 0 && fq.getPurpose() == FuelPurpose.TRAVEL) {
					switch (fq.getFuelType()) {
					case BASE_FUEL:
						r = 255;
						break;
					case FBO:
						b = 255;
						break;
					case NBO:
						g = 255;
						break;
					}
				}
			}
			
			return ColorCache.getColor(r, g, b);
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
