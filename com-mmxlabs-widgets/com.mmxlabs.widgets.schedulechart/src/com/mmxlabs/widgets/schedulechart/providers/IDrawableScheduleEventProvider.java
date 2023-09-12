/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.providers;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.IScheduleChartSettings;
import com.mmxlabs.widgets.schedulechart.ScheduleCanvasState;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.ScheduleEventAnnotation;
import com.mmxlabs.widgets.schedulechart.draw.DrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEventAnnotation;

public interface IDrawableScheduleEventProvider {
	
	DrawableScheduleEvent createDrawableScheduleEvent(ScheduleEvent se, Rectangle bounds, ScheduleCanvasState canvasState);

	DrawableScheduleEventAnnotation createDrawableScheduleEventAnnotation(ScheduleEventAnnotation sea, DrawableScheduleEvent dse, ToIntFunction<LocalDateTime> timeToXFunc, ScheduleCanvasState canvasState, IScheduleChartSettings settings);
	
	/**
	 * Gets the render order comparator for events and annotations within a DrawableScheduleChartRow
	 * @return
	 */
	Comparator<DrawableElement> getEventAndAnnotationRenderOrderComparator();
}
