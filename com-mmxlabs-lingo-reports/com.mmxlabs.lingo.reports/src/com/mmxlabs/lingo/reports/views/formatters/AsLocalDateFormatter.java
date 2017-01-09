/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.formatters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.mmxlabs.models.ui.tabular.BaseFormatter;

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
		if (object instanceof LocalDate) {
			LocalDate localDate = (LocalDate)object;
			return localDate.format(dateFormat);
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
			return LocalDateTime.now().withYear(Year.MIN_VALUE);
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