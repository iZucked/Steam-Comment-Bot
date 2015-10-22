/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.formatters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import org.eclipse.swt.widgets.DateTime;

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
		final ZonedDateTime dateTime = getDateTime(object);
		if (dateTime != null) {
			return dateFormat.print(dateTime) + (showZone ? (" (" + dateTime.getZone().toTimeZone().getDisplayName(false, TimeZone.SHORT) + ")") : "");
		}
		return null;
	}

	protected ZonedDateTime getDateTime(final Object object) {
		ZonedDateTime localDate = null;
		if (object instanceof DateTime) {
			localDate = (ZonedDateTime) object;
		} else if (object instanceof LocalDate) {
			final LocalDate dateTime = (LocalDate) object;
			localDate = dateTime.toDateTimeAtStartOfDay(ZoneId.of("UTC"));
		} else if (object instanceof LocalDateTime) {
			final LocalDateTime dateTime = (LocalDateTime) object;
			localDate = dateTime.toDateTime(ZoneId.of("UTC"));
		}
		return localDate;
	}

	@Override
	public Comparable<?> getComparable(final Object object) {
		final ZonedDateTime localDate = getDateTime(object);
		if (localDate == null) {
			FIXME localDate= ? LocalDate.of(Year.MIN_VALUE, 1, 1);
		}
		return localDate;
	}

	@Override
	public Object getFilterValue(final Object object) {
		final ZonedDateTime localDate = getDateTime(object);
		if (localDate != null) {
			return localDate;
		}
		return object;
	}
}