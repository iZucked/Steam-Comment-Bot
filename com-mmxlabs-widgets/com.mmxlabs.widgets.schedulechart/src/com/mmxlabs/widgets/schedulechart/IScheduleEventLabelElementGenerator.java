package com.mmxlabs.widgets.schedulechart;

import org.eclipse.swt.graphics.Color;

import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEventLabel;

public interface IScheduleEventLabelElementGenerator {
	DrawableScheduleEventLabel generate(final ScheduleEvent se, Color labelTextColour, Color labelBackgroundColor);

	// Use this for testing when colour doesn't matter
	default DrawableScheduleEventLabel generate(final ScheduleEvent se) {
		return generate(se, null, null);
	}
}
