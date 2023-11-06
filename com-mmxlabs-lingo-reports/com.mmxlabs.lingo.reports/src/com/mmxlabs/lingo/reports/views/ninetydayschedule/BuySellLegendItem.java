package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.util.List;
import java.util.function.BiFunction;

import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;
import com.mmxlabs.widgets.schedulechart.providers.ILegendItem;

public class BuySellLegendItem implements ILegendItem {

	private final String description;
	private final ScheduleEvent dummyEvent;
	private final Rectangle dummyBounds;
	private final BiFunction<ScheduleEvent, Rectangle, List<DrawableScheduleEvent>> getDrawableEvents;
	
	public BuySellLegendItem(String description, BiFunction<ScheduleEvent, Rectangle, List<DrawableScheduleEvent>> getDrawableEvents) {
		this.description = description;
		this.dummyEvent = getDummyEvent();
		this.dummyBounds = getDummyBounds();
		this.getDrawableEvents = getDrawableEvents;
	}
	
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public int getItemWidth() {
		return 2;
	}
	
	@Override
	public int getItemHeight() {
		return 15;
	}
	
	@Override
	public boolean showBackground() {
		return false;
	}

	@Override
	public List<DrawableScheduleEvent> getDrawableEvents() {
		return getDrawableEvents.apply(dummyEvent, dummyBounds);
	}

}
