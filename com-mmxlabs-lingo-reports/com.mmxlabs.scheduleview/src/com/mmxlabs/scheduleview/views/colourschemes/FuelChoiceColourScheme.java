/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduleview.views.colourschemes;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SlotVisit;

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

			for (final FuelQuantity fq : journey.getFuels()) {
				if (fq.getAmounts().isEmpty() == false) {
					if (fq.getFuelName().equals("Base Fuel")) {
						r = 255;
						break;
					} else if (fq.getFuelName().equals("FBO")) {
						b = 255;
						break;
					} else if (fq.getFuelName().equals("NBO")) {
						g = 255;
						break;
					}
				}
			}
			
			if (b > 0) {
				r = 0; // don't show pilot
			}

			return ColorCache.getColor(r, g, b);
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
