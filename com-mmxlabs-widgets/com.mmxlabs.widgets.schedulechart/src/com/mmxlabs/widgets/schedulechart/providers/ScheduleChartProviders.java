/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.providers;

public record ScheduleChartProviders(
		IDrawableScheduleEventLabelProvider labelProvider,
		IDrawableScheduleEventProvider drawableEventProvider,
		IDrawableScheduleEventTooltipProvider drawableTooltipProvider,
		IScheduleChartSortingProvider sortingProvider
) {}
