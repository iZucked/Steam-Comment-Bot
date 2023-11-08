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

public class DryDockEvent extends NinetyDayDrawableScheduleEvent {

	public DryDockEvent(ScheduleEvent se, Rectangle bounds, boolean noneSelected) {
		super(se, bounds, noneSelected);
		
		// Fixing missing bottom pixel
		Rectangle newBounds = getBounds();
		newBounds.height += 1;
		setBounds(newBounds);
	}

	@Override
	public Color getBackgroundColour() {
		return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Event_DryDock, ColourElements.Background);
	}

	@Override
	protected Color getBorderColour() {
		return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Event_DryDock, ColourElements.Border);
	}

}
