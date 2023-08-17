package com.mmxlabs.widgets.schedulechart.providers;

import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.ScheduleCanvasState;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;

public interface IDrawableScheduleEventProvider {
	
	DrawableScheduleEvent creatDrawableScheduleEvent(ScheduleEvent se, Rectangle bounds, ScheduleCanvasState canvasState);

}
