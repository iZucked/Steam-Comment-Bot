/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.draw;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.eclipse.swt.graphics.Rectangle;
import com.mmxlabs.widgets.schedulechart.IScheduleChartColourScheme;
import com.mmxlabs.widgets.schedulechart.IScheduleChartSettings;
import com.mmxlabs.widgets.schedulechart.ScheduleCanvasState;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRow;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRowPriorityType;
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

	private final boolean isNoSpacer;
	private final ScheduleChartRow scr;
	private final ScheduleCanvasState canvasState;
	private final ScheduleTimeScale sts;
	private final int rowNum;
	private List<DrawableScheduleEvent> lastDrawnEvents = new ArrayList<>();
	private List<DrawableScheduleEventAnnotation> lastDrawnAnnotations = new ArrayList<>();
	private int actualHeight = -1;
	private boolean showsAnnotations;

	public DrawableScheduleChartRow(final ScheduleChartRow scr, final ScheduleCanvasState canvasState, final int rowNum, ScheduleTimeScale sts, IDrawableScheduleEventProvider drawableEventProvider, IScheduleEventStylingProvider eventStylingProvider,
			IScheduleChartSettings settings, final boolean noSpacer) {
		this.scr = scr;
		this.canvasState = canvasState;
		this.sts = sts;
		this.rowNum = rowNum;
		this.colourScheme = settings.getColourScheme();
		this.eventStylingProvider = eventStylingProvider;
		this.settings = settings;
		this.drawableEventProvider = drawableEventProvider;
		this.isNoSpacer = noSpacer;
		this.showsAnnotations = true;
		this.showsAnnotations = scr.getEvents().stream().anyMatch(e -> e.showsAnnotations());
		this.actualHeight = getActualHeight();
	}
	
	public int getActualHeight() {
		if(scr.getRowType().equals(ScheduleChartRowPriorityType.REGULAR_ROWS)) {
			int rowHeight = showsAnnotations ? settings.getRowHeightWithAnnotations() : settings.getRowHeight();
			int rowHeightWithSpacer = isNoSpacer ? rowHeight - 2 * settings.getSpacerWidth() : rowHeight;
			return (settings.hasMultipleScenarios() && !settings.inCompareMode()) ? rowHeightWithSpacer + 15 : rowHeightWithSpacer;
		}
		else {
			return settings.getBuySellRowHeight();
		}
	}

	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
		List<BasicDrawableElement> res = new ArrayList<>();

		// Add background
		res.add(BasicDrawableElements.Rectangle.withBounds(bounds.x, bounds.y, bounds.width, actualHeight).bgColour(rowNum % 2 == 0 ? null : colourScheme.getRowBgColour(rowNum))
				.borderColour(colourScheme.getGridStrokeColour()).alpha(160).create());
		
		lastDrawnEvents.clear();
		lastDrawnAnnotations.clear();
		for (final ScheduleEvent se : scr.getEvents()) {
			if (!isWithinBounds(se, bounds)) continue;
			DrawableScheduleEvent drawableEvent = createDrawableScheduleEvent(se, bounds);
			if (drawableEvent == null) {
				continue;
			}
			drawableEvent.setStylingProvider(getStylingProvider());
			lastDrawnEvents.add(drawableEvent);
			if (!settings.showAnnotations()) {
				//continue;
			}
			for (ScheduleEventAnnotation sea : se.getAnnotations()) {
				DrawableScheduleEventAnnotation drawableAnnotation = drawableEventProvider.createDrawableScheduleEventAnnotation(sea, drawableEvent, sts::getXForDateTime, canvasState, settings);
				if (drawableAnnotation == null) {
					continue;
				}
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
	
	private boolean isWithinBounds(ScheduleEvent se, Rectangle rowBounds) {
		int minX = rowBounds.x;
		int maxX = rowBounds.x + rowBounds.width;
		boolean isEventWithinBounds = sts.getXForDateTime(se.getEnd()) >= minX || sts.getXForDateTime(se.getStart()) <= maxX;
		return isEventWithinBounds || se.getAnnotations().stream().anyMatch(sea -> isAnnotationWithinBounds(sea, rowBounds));
	}
	
	private boolean isAnnotationWithinBounds(ScheduleEventAnnotation sea, Rectangle rowBounds) {
		int minX = rowBounds.x;
		int maxX = rowBounds.x + rowBounds.width;
		if (sea.getDates().isEmpty()) return true;
		LocalDateTime minDate = sea.getDates().stream().min(Comparator.naturalOrder()).get();
		LocalDateTime maxDate = sea.getDates().stream().min(Comparator.naturalOrder()).get();
		return sts.getXForDateTime(maxDate) >= minX || sts.getXForDateTime(minDate) <= maxX;
	}

	private DrawableScheduleEvent createDrawableScheduleEvent(ScheduleEvent se, Rectangle bounds) {
		final int spacer = isNoSpacer ? 0 : settings.getSpacerWidth();
		
		final int eventHeight = scr.getRowType().equals(ScheduleChartRowPriorityType.REGULAR_ROWS) ? settings.getEventHeight() : settings.getBuySellEventHeight();

		int startX = sts.getXForDateTime(se.getStart());
		int endX = sts.getXForDateTime(se.getEnd());
		
		int y = bounds.y + (settings.showAnnotations() && se.showsAnnotations() ? settings.getTopAnnotationHeight() + spacer : 0) + spacer;
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
