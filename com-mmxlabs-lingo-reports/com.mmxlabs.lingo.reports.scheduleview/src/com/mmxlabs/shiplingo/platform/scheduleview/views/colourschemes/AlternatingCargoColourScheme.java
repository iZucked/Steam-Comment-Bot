/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.SlotVisit;

public class AlternatingCargoColourScheme extends ColourScheme {

	private final Color baseColor;
	private final Color secondaryColor;

	private Color selectedColor;

	public AlternatingCargoColourScheme() {
		this(ColorCache.getColor(220, 20, 50), ColorCache.getColor(20, 155, 124));
	}

	public AlternatingCargoColourScheme(final Color baseColor, final Color second) {
		this.baseColor = baseColor;
		this.secondaryColor = second;
		selectedColor = baseColor;
	}

	@Override
	public String getName() {
		return "Alternating Cargoes";
	}

	@Override
	public Color getBackground(final Object element) {
		if (element instanceof SlotVisit && ((SlotVisit) element).getSlotAllocation().getSlot() instanceof LoadSlot) {
			// flip colour
			if (selectedColor == baseColor) {
				selectedColor = secondaryColor;
			} else {
				selectedColor = baseColor;
			}
		}
		return selectedColor;
	}
}
