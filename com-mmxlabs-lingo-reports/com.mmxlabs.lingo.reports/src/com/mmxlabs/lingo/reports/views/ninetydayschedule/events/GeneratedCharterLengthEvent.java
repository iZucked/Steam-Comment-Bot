package com.mmxlabs.lingo.reports.views.ninetydayschedule.events;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.lingo.reports.ColourPalette.ColourElements;
import com.mmxlabs.lingo.reports.ColourPalette.ColourPaletteItems;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;

public class GeneratedCharterLengthEvent extends NinetyDayDrawableScheduleEvent {

	public GeneratedCharterLengthEvent(ScheduleEvent se, Rectangle bounds, boolean noneSelected) {
		super(se, bounds, noneSelected);
	}

	@Override
	protected Color getBackgroundColour() {
		return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_GeneratedCharterLength, ColourElements.Background);
	}

	@Override
	protected Color getBorderColour() {
		return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_GeneratedCharterLength, ColourElements.Border);
	}

}