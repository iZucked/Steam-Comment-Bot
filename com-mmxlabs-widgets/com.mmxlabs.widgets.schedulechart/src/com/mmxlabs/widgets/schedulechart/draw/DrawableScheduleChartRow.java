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
		for (ScheduleEvent se : scr.getEvents()) {
			DrawableScheduleEvent drawableEvent = createDrawableScheduleEvent(se, bounds);
			if (drawableEvent == null) continue;
			lastDrawnEvents.add(drawableEvent);
		}
		
		// Draw resizable events first
		lastDrawnEvents.sort(Comparator.comparing(dse -> ((DrawableScheduleEvent) dse).getScheduleEvent().isResizable()).reversed());

		res.addAll(lastDrawnEvents.stream().flatMap(dse -> dse.getBasicDrawableElements(queryResolver).stream()).toList());
		return res;
	}
	
	private DrawableScheduleEvent createDrawableScheduleEvent(ScheduleEvent se, Rectangle bounds) {
		final int spacer = settings.getSpacerWidth();
		final int eventHeight = se.isResizable() && settings.showWindows() ? settings.getWindowedEventHeight() : settings.getEventHeight();

		int startX = sts.getXForDateTime(settings.showWindows() && se.getWindowStartDate() != null ? se.getWindowStartDate() : se.getStart());
		int endX = sts.getXForDateTime(settings.showWindows() && se.getWindowEndDate() != null ? se.getWindowEndDate() : se.getEnd());
		int y = bounds.y + (settings.showWindows() && !se.isResizable() ? (settings.getWindowedEventHeight() - settings.getEventHeight()) / 2 : 0) + spacer;
		Rectangle eventBounds = new Rectangle(startX, y, endX - startX, eventHeight);
		return drawableEventProvider.createDrawableScheduleEvent(se, eventBounds, canvasState);
	}

	public List<DrawableScheduleEvent>  getLastDrawnEvents() {
		return lastDrawnEvents;
	}
	
	public ScheduleChartRow getScheduleChartRow() {
		return scr;
	}


}
