package com.mmxlabs.widgets.schedulechart.providers;

public record ScheduleChartProviders(
		IScheduleEventLabelProvider labelProvider,
		IDrawableScheduleEventProvider drawableEventProvider,
		IDrawableScheduleEventTooltipProvider drawableTooltipProvider
) {}
