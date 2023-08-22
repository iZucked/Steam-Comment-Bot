/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.providers;

public record ScheduleChartProviders(
		IScheduleEventLabelProvider labelProvider,
		IDrawableScheduleEventProvider drawableEventProvider,
		IDrawableScheduleEventTooltipProvider drawableTooltipProvider
) {}
