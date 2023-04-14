package com.mmxlabs.lingo.reports.views.formatters;

import com.mmxlabs.lingo.reports.views.formatters.Formatters.DurationMode;

public class ScheduleChartFormatters {

	private ScheduleChartFormatters() {
	}

	private static DurationMode durationMode = null;

	public static DurationMode getDurationMode() {
		return durationMode != null ? durationMode : Formatters.getDurationMode();
	}

	public static void setDurationMode(final DurationMode mode) {
		durationMode = mode;
	}

	public static String formatAsDays(final long hours) {
		if (durationMode != null) {
			return Formatters.formatAsDays(durationMode, hours);
		}
		return Formatters.formatAsDays(hours);
	}
}
