/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule.annotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.function.ToIntFunction;

import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.IScheduleChartSettings;
import com.mmxlabs.widgets.schedulechart.ScheduleEventAnnotation;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;

public class NinetyDaySlotVisitLatenessDefaultAnnotation extends NinetyDaySlotVisitLatenessAnnotation {
	
	private static final int TOP_PADDING = 2;
	
	public NinetyDaySlotVisitLatenessDefaultAnnotation(ScheduleEventAnnotation annotation, DrawableScheduleEvent dse, IScheduleChartSettings settings, ToIntFunction<LocalDateTime> f) {
		super(annotation, dse, settings, f);
	}

	@Override
	protected Rectangle calculateAnnotationBounds(Rectangle scheduleEventBounds) {
		Arrays.sort(dateXCoords);
		final int x = dateXCoords[0];
		final int width = dateXCoords[1] - x + scheduleEventBounds.width;
		
		final int spacer = settings.getSpacerWidth();
		final int y = scheduleEventBounds.y - spacer;
		final int height = (getHeight(scheduleEventBounds) / 2) - TOP_PADDING;
		return new Rectangle(x, y, width, height);
	}
}
