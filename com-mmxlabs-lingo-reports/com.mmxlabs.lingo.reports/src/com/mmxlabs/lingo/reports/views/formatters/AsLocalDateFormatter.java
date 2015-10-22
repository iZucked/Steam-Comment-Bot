/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.formatters;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class AsLocalDateFormatter extends BaseFormatter {
	final DateTimeFormatter dateFormat;

	public AsLocalDateFormatter(final DateTimeFormatter dateFormat) {
		this.dateFormat = dateFormat;
	}

	@Override
	public String render(final Object object) {
		if (object == null) {
			return "";
		}
		LocalDateTime localDate = getLocalDate(object);
		if (localDate != null) {
			return localDate.format(dateFormat);
		}
		return null;
	}

	protected LocalDateTime getLocalDate(final Object object) {
		LocalDateTime localDate = null;
		if (object instanceof LocalDateTime) {
			localDate = (LocalDateTime) object;
		} else if (object instanceof ZonedDateTime) {
			ZonedDateTime dateTime = (ZonedDateTime) object;
			localDate = dateTime.toLocalDateTime();
		}
		return localDate;
	}

	@Override
	public Comparable<?> getComparable(final Object object) {
		LocalDateTime localDate = getLocalDate(object);
		if (object == null) {
			localDate.withYear(Year.MIN_VALUE);
		}
		return localDate;
	}

	@Override
	public Object getFilterValue(final Object object) {
		LocalDateTime localDate = getLocalDate(object);
		if (localDate != null) {
			return localDate;
		}
		return object;
	}
}