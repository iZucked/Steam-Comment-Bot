/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduleview.views.colourschemes;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import scenario.schedule.events.FuelMixture;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.FuelType;
import scenario.schedule.events.Idle;

/**
 * A colour scheme which highlights cooldown in red
 * 
 * @author hinton
 *
 */
public class CooldownColourScheme implements IScheduleViewColourScheme {
	@Override
	public String getName() {
		return "Cooldown";
	}

	@Override
	public Color getForeground(Object element) {
		return null;
	}

	@Override
	public Color getBackground(final Object element) {
		if (element instanceof FuelMixture) {
			for (final FuelQuantity fq : ((FuelMixture) element).getFuelUsage()) {
				if (fq.getFuelType() == FuelType.COOLDOWN && fq.getQuantity() > 0) {
					return ColorCache.getColor(255, 0, 0);
				}
			}
		}
		if (element instanceof Idle) {
			return ColorCache.getColor(0,0,255);
		}
		return null;
	}
}
