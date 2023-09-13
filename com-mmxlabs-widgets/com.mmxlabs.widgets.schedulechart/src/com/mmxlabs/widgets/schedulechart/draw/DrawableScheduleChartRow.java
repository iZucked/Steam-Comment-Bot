/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.draw;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.IScheduleChartColourScheme;
import com.mmxlabs.widgets.schedulechart.IScheduleChartSettings;
import com.mmxlabs.widgets.schedulechart.ScheduleCanvasState;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRow;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.ScheduleEventAnnotation;
import com.mmxlabs.widgets.schedulechart.ScheduleTimeScale;
import com.mmxlabs.widgets.schedulechart.providers.IDrawableScheduleEventProvider;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleEventStylingProvider;

public class DrawableScheduleChartRow extends DrawableElement {

	private final IScheduleChartColourScheme colourScheme;
	private final IScheduleEventStylingProvider eventStylingProvider;
	private final IScheduleChartSettings settings;
	private final IDrawableScheduleEventProvider drawableEventProvider;

	private final ScheduleChartRow scr;
	private final ScheduleCanvasState canvasState;
	private final ScheduleTimeScale sts;
	private final int rowNum;
	private List<DrawableScheduleEvent> lastDrawnEvents = new ArrayList<>();
	private List<DrawableScheduleEventAnnotation> lastDrawnAnnotations = new ArrayList<>();

	public DrawableScheduleChartRow(final ScheduleChartRow scr, final ScheduleCanvasState canvasState, final int rowNum, ScheduleTimeScale sts, IDrawableScheduleEventProvider drawableEventProvider, IScheduleEventStylingProvider eventStylingProvider,
			IScheduleChartSettings settings) {
		this.scr = scr;
		this.canvasState = canvasState;
		this.sts = sts;
		this.rowNum = rowNum;
		this.colourScheme = settings.getColourScheme();
		this.eventStylingProvider = eventStylingProvider;
		this.settings = settings;
		this.drawableEventProvider = drawableEventProvider;
	}

	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
		List<BasicDrawableElement> res = new ArrayList<>();

		final int rowHeight = settings.getRowHeight();

		// Add background
		res.add(BasicDrawableElements.Rectangle.withBounds(bounds.x, bounds.y, bounds.width, rowHeight).bgColour(rowNum % 2 == 0 ? null : colourScheme.getRowBgColour(rowNum))
				.borderColour(colourScheme.getGridStrokeColour()).alpha(160).create());
		
		lastDrawnEvents.clear();
		lastDrawnAnnotations.clear();
		for (ScheduleEvent se : scr.getEvents()) {
			DrawableScheduleEvent drawableEvent = createDrawableScheduleEvent(se, bounds);
			if (drawableEvent == null) continue;
			lastDrawnEvents.add(drawableEvent);
			if (!settings.showAnnotations()) continue;
			for (ScheduleEventAnnotation sea : se.getAnnotations()) {
				DrawableScheduleEventAnnotation drawableAnnotation = drawableEventProvider.createDrawableScheduleEventAnnotation(sea, drawableEvent, sts::getXForDateTime, canvasState, settings);
				if (drawableAnnotation == null) continue;
				lastDrawnAnnotations.add(drawableAnnotation);
			}
		}
		
		List<DrawableElement> toDraw = new ArrayList<>();
		toDraw.addAll(lastDrawnEvents);
		toDraw.addAll(lastDrawnAnnotations);
		toDraw.sort(drawableEventProvider.getEventAndAnnotationRenderOrderComparator());

		res.addAll(toDraw.stream().flatMap(dse -> dse.getBasicDrawableElements(queryResolver).stream()).toList());
		return res;
	}
	
	private DrawableScheduleEvent createDrawableScheduleEvent(ScheduleEvent se, Rectangle bounds) {
		final int spacer = settings.getSpacerWidth();
		final int eventHeight = settings.getEventHeight();

		int startX = sts.getXForDateTime(se.getStart());
		int endX = sts.getXForDateTime(se.getEnd());
		int y = bounds.y + (settings.showAnnotations() ? settings.getTopAnnotationHeight() + spacer : 0) + spacer;
		Rectangle eventBounds = new Rectangle(startX, y, endX - startX, eventHeight);
		return drawableEventProvider.createDrawableScheduleEvent(se, eventBounds, canvasState);
	}

	public List<DrawableScheduleEvent>  getLastDrawnEvents() {
		return lastDrawnEvents;
	}
	
	public List<DrawableScheduleEventAnnotation> getLastDrawnAnnotations() {
		return lastDrawnAnnotations;
	}
	
	public ScheduleChartRow getScheduleChartRow() {
		return scr;
	}
	
	public int getRowNum() {
		return rowNum;
	}


}
