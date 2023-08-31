package com.mmxlabs.widgets.schedulechart;

import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEventLabel;

public interface IScheduleEventLabelElementGenerator {
	DrawableScheduleEventLabel generate(final ScheduleEvent se, final DrawableScheduleEvent dse);

	// Use this for testing when styling doesn't matter
	default DrawableScheduleEventLabel generate(final ScheduleEvent se) {
		return generate(se, null);
	}
}
