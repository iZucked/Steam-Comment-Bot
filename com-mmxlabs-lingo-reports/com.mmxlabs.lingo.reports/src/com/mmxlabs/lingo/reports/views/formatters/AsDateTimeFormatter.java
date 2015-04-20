/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.formatters;

import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;

public class AsDateTimeFormatter extends BaseFormatter {
	final DateTimeFormatter dateFormat;
	final boolean showZone;

	public AsDateTimeFormatter(final DateTimeFormatter dateFormat, final boolean showZone) {
		this.dateFormat = dateFormat;
		this.showZone = showZone;
	}

	@Override
	public String render(final Object object) {
		if (object == null) {
			return "";
		}
		final DateTime dateTime = getDateTime(object);
		if (dateTime != null) {
			return dateFormat.print(dateTime) + (showZone ? (" (" + dateTime.getZone().toTimeZone().getDisplayName(false, TimeZone.SHORT) + ")") : "");
		}
		return null;
	}

	protected DateTime getDateTime(final Object object) {
		DateTime localDate = null;
		if (object instanceof DateTime) {
			localDate = (DateTime) object;
		} else if (object instanceof LocalDate) {
			final LocalDate dateTime = (LocalDate) object;
			localDate = dateTime.toDateTimeAtStartOfDay(DateTimeZone.UTC);
		} else if (object instanceof LocalDateTime) {
			final LocalDateTime dateTime = (LocalDateTime) object;
			localDate = dateTime.toDateTime(DateTimeZone.UTC);
		}
		return localDate;
	}

	@Override
	public Comparable<?> getComparable(final Object object) {
		final DateTime localDate = getDateTime(object);
		if (localDate == null) {
			new LocalDate(2000, 1, 1).year().withMinimumValue();
		}
		return localDate;
	}

	@Override
	public Object getFilterValue(final Object object) {
		final DateTime localDate = getDateTime(object);
		if (localDate != null) {
			return localDate;
		}
		return object;
	}
}