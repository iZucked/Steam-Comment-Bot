package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.util.List;
import java.util.function.BiFunction;
import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;
import com.mmxlabs.widgets.schedulechart.providers.ILegendItem;

public class DefaultLegendItem implements ILegendItem {

	private final String description;
	private final ScheduleEvent dummyEvent;
	private final Rectangle dummyBounds;
	private final BiFunction<ScheduleEvent, Rectangle, List<DrawableScheduleEvent>> getDrawableEvents;
	
	public DefaultLegendItem(String description, BiFunction<ScheduleEvent, Rectangle, List<DrawableScheduleEvent>> getDrawableEvents) {
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
		return 10;
	}
	
	@Override
	public int getItemHeight() {
		return 10;
	}

	@Override
	public List<DrawableScheduleEvent> getDrawableEvents() {
		List<DrawableScheduleEvent> events =  this.getDrawableEvents.apply(dummyEvent, dummyBounds);
		events.forEach(e -> e.setStylingProvider(new NinetyDayScheduleEventStylingProvider()));
		return events;
	}
}
