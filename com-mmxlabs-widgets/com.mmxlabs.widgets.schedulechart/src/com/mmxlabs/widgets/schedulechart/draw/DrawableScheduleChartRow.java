package com.mmxlabs.widgets.schedulechart.draw;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.IScheduleChartColourScheme;
import com.mmxlabs.widgets.schedulechart.IScheduleChartSettings;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRow;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.ScheduleTimeScale;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleEventStylingProvider;

public class DrawableScheduleChartRow extends DrawableElement {

	private final IScheduleChartColourScheme colourScheme;
	private final IScheduleEventStylingProvider eventStylingProvider;
	private final IScheduleChartSettings settings;

	private final ScheduleChartRow scr;
	private final ScheduleTimeScale sts;
	private final int rowNum;
	private final Set<ScheduleEvent> selectedEvents;
	private List<DrawableScheduleEvent> lastDrawnEvents = new ArrayList<>();

	public DrawableScheduleChartRow(final ScheduleChartRow scr, final int rowNum, ScheduleTimeScale sts, IScheduleChartColourScheme colourScheme, IScheduleEventStylingProvider eventStylingProvider,
			IScheduleChartSettings settings, Set<ScheduleEvent> selectedEvents) {
		this.scr = scr;
		this.sts = sts;
		this.rowNum = rowNum;
		this.colourScheme = colourScheme;
		this.eventStylingProvider = eventStylingProvider;
		this.settings = settings;
		this.selectedEvents = selectedEvents;
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
		for (ScheduleEvent se : scr.getEvents()) {
			int startX = sts.getXForDateTime(se.getStart());
			int endX = sts.getXForDateTime(se.getEnd());
			Rectangle eventBounds = new Rectangle(startX, bounds.y + spacer, endX - startX, eventHeight);
			DrawableScheduleEvent drawableEvent = new DrawableScheduleEvent(se, eventBounds, eventStylingProvider, selectedEvents.isEmpty());
			lastDrawnEvents.add(drawableEvent);
			res.addAll(drawableEvent.getBasicDrawableElements());
		}

		return res;
	}
	

	public List<DrawableScheduleEvent>  getLastDrawnEvents() {
		return lastDrawnEvents;
	}


}
