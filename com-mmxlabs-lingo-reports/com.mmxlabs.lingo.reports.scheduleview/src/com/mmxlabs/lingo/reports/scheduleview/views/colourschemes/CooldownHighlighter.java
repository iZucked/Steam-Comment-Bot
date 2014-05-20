/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views.colourschemes;

import static com.mmxlabs.lingo.reports.ColourPalette.Warning_Yellow;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.models.lng.schedule.Cooldown;

public class CooldownHighlighter extends ColourScheme {

	@Override
	public String getName() {
		return "Cooldown";
	}

	@Override
	public Color getBackground(final Object element) {
		if (element instanceof Cooldown) {
			return ColorCache.getColor(Warning_Yellow);
		}
		return null;
	}
}
