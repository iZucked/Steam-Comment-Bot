/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule.events;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.lingo.reports.ColourPalette.ColourElements;
import com.mmxlabs.lingo.reports.ColourPalette.ColourPaletteItems;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;

public class GeneratedCharterOutEvent extends NinetyDayDrawableScheduleEvent {

	public GeneratedCharterOutEvent(ScheduleEvent se, Rectangle bounds, boolean noneSelected) {
		super(se, bounds, noneSelected);
		Rectangle newBounds = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
		newBounds.height += 1;
		setBounds(newBounds);
	}

	@Override
	public Color getBackgroundColour() {
		return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_GeneratedCharterOut, ColourElements.Background);
	}

	@Override
	protected Color getBorderColour() {
		return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_GeneratedCharterOut, ColourElements.Border);
	}

}
