package com.mmxlabs.widgets.schedulechart.providers;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;

public interface ILegendItem {
	String getDescription();
	List<DrawableScheduleEvent> getDrawableEvents();
	int getItemWidth();
	int getItemHeight();
	
	default ScheduleEvent getDummyEvent() {
		ScheduleEvent e = new ScheduleEvent(LocalDateTime.now(), LocalDateTime.now(), new Object(), "", Collections.emptyList(), false);
		e.forceVisible();
		return e;
	}
	
	default Rectangle getDummyBounds() {
		return new Rectangle(0, 0, 10, 10);
	}
	
	default boolean showBackground() {
		return true;
	}
}
