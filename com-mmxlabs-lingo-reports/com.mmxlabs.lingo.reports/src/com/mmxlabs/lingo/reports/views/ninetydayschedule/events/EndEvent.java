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

public class EndEvent extends NinetyDayDrawableScheduleEvent {
	private static final int EVENT_WIDTH = 10;
	
	public EndEvent(ScheduleEvent se, Rectangle bounds, boolean noneSelected) {
		super(se, bounds, noneSelected);
		final Rectangle newBounds = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
		newBounds.width = EVENT_WIDTH;
		setBounds(newBounds);
	}

	@Override
	public Color getBackgroundColour() {
		return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_End, ColourElements.Background);
	}

	@Override
	protected Color getBorderColour() {
		return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_End, ColourElements.Background);
	}

}
