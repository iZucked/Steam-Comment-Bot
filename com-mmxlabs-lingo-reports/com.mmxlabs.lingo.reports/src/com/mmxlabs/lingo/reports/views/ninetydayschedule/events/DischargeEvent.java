/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule.events;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.lingo.reports.ColourPalette.ColourElements;
import com.mmxlabs.lingo.reports.ColourPalette.ColourPaletteItems;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;

public class DischargeEvent extends NinetyDayDrawableScheduleEvent {
	
	public DischargeEvent(ScheduleEvent se, Rectangle bounds, boolean noneSelected) {
		super(se, bounds, noneSelected);
	}	

	@Override
	public Color getBackgroundColour() {
		return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Discharge, ColourElements.Background);
	}

	@Override
	protected Color getBorderColour() {
		return Display.getDefault().getSystemColor(SWT.COLOR_DARK_YELLOW);
//		return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Discharge, ColourElements.Border);
	}

}
