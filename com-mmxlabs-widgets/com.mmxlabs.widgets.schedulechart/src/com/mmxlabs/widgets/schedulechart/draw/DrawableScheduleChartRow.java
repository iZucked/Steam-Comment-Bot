/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.draw;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.IScheduleChartColourScheme;
import com.mmxlabs.widgets.schedulechart.IScheduleChartSettings;
import com.mmxlabs.widgets.schedulechart.ScheduleCanvasState;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.ScheduleTimeScale;
import com.mmxlabs.widgets.schedulechart.providers.IDrawableScheduleEventProvider;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleEventStylingProvider;

public class DrawableScheduleChartRow extends DrawableElement {

	private final IScheduleChartColourScheme colourScheme;
	private final IScheduleEventStylingProvider eventStylingProvider;
	private final IScheduleChartSettings settings;
	private final IDrawableScheduleEventProvider drawableEventProvider;

	private final ScheduleCanvasState canvasState;
	private final ScheduleTimeScale sts;
	private final int rowNum;
	private List<DrawableScheduleEvent> lastDrawnEvents = new ArrayList<>();

	public DrawableScheduleChartRow(final ScheduleCanvasState canvasState, final int rowNum, ScheduleTimeScale sts, IDrawableScheduleEventProvider drawableEventProvider, IScheduleEventStylingProvider eventStylingProvider,
			IScheduleChartSettings settings) {
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

		final int spacer = settings.getSpacerWidth();
		final int eventHeight = settings.getRowEventHeight();
		final int rowHeight = settings.getRowHeight();

		// Add background
		res.add(BasicDrawableElements.Rectangle.withBounds(bounds.x, bounds.y, bounds.width, rowHeight).bgColour(rowNum % 2 == 0 ? null : colourScheme.getRowBgColour(rowNum))
				.borderColour(colourScheme.getGridStrokeColour()).alpha(160).create());
		
		lastDrawnEvents.clear();
		for (ScheduleEvent se : canvasState.getRows().get(rowNum).getEvents()) {
			int startX = sts.getXForDateTime(se.getStart());
			int endX = sts.getXForDateTime(se.getEnd());
			Rectangle eventBounds = new Rectangle(startX, bounds.y + spacer, endX - startX, eventHeight);
			DrawableScheduleEvent drawableEvent = drawableEventProvider.createDrawableScheduleEvent(se, eventBounds, canvasState);
			if (drawableEvent == null) continue;
			lastDrawnEvents.add(drawableEvent);
			res.addAll(drawableEvent.getBasicDrawableElements());
		}

		return res;
	}
	

	public List<DrawableScheduleEvent>  getLastDrawnEvents() {
		return lastDrawnEvents;
	}


}
