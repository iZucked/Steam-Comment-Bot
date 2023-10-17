/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule.annotations;

import java.time.LocalDateTime;
import java.util.function.ToIntFunction;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.lingo.reports.ColourPalette.ColourElements;
import com.mmxlabs.lingo.reports.ColourPalette.ColourPaletteItems;
import com.mmxlabs.widgets.schedulechart.IScheduleChartSettings;
import com.mmxlabs.widgets.schedulechart.ScheduleEventAnnotation;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;

public class NinetyDaySlotVisitLatenessAnnotation extends NinetyDaySlotWindowAnnotation {

	public NinetyDaySlotVisitLatenessAnnotation(ScheduleEventAnnotation annotation, DrawableScheduleEvent dse, IScheduleChartSettings settings, ToIntFunction<LocalDateTime> f) {
		super(annotation, dse, settings, f);
	}
	
	@Override
	protected Color getBorderColour() {
		return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Late_Event, ColourElements.Background);
	}

	@Override
	protected Color getBgColour() {
		return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Late_Event, ColourElements.Background);
	}
	
}
