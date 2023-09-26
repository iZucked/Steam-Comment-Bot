/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.providers;

import java.util.Optional;

import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.ScheduleEventTooltip;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEventTooltip;

public interface IDrawableScheduleEventTooltipProvider {
	
	Optional<ScheduleEventTooltip> getTooltip(ScheduleEvent event);
	
	Optional<DrawableScheduleEventTooltip> getDrawableTooltip(ScheduleEventTooltip tooltip);
	
	default Optional<DrawableScheduleEventTooltip> getDrawableTooltip(ScheduleEvent event) {
		return this.getTooltip(event).flatMap(this::getDrawableTooltip);
	}
}
