/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.shiplingo.platform.scheduleview.views.IScheduleViewColourScheme;

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
	public Color getForeground(final Object element) {
		return null;
	}

	@Override
	public Color getBackground(final Object element) {
		if (element instanceof Cooldown) {
			return ColorCache.getColor(255, 0, 0);
		}
		if (element instanceof Idle) {
			return ColorCache.getColor(0, 0, 255);
		}
		return null;
	}

	@Override
	public int getAlpha(final Object element) {
		return 255;
	}

	@Override
	public Color getBorderColour(final Object element) {
		return null;
	}
}
