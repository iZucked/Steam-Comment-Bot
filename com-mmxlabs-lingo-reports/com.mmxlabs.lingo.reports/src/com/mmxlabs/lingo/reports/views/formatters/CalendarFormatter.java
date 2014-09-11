/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.formatters;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CalendarFormatter extends BaseFormatter {
	final DateFormat dateFormat;
	final boolean showZone;

	public CalendarFormatter(final DateFormat dateFormat, final boolean showZone) {
		this.dateFormat = dateFormat;
		this.showZone = showZone;
	}

	@Override
	public String format(final Object object) {
		if (object == null) {
			return "";
		}
		final Calendar cal = (Calendar) object;

		dateFormat.setCalendar(cal);
		return dateFormat.format(cal.getTime()) + (showZone ? (" (" + cal.getTimeZone().getDisplayName(false, TimeZone.SHORT) + ")") : "");
	}

	@Override
	public Comparable getComparable(final Object object) {
		if (object == null) {
			return new Date(-Long.MAX_VALUE);
		}
		return ((Calendar) object).getTime();
	}

	@Override
	public Object getFilterable(final Object object) {
		if (object instanceof Calendar) {
			return object;
		}
		return object;
	}
}