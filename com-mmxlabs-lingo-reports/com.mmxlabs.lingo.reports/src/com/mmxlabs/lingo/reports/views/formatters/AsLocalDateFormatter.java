/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.formatters;

import java.text.DateFormat;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

public class AsLocalDateFormatter extends BaseFormatter {
	final DateFormat dateFormat;

	public AsLocalDateFormatter(final DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	@Override
	public String render(final Object object) {
		if (object == null) {
			return "";
		}
		LocalDateTime localDate = getLocalDate(object);
		if (localDate != null) {
			return dateFormat.format(localDate.toDate());
		}
		return null;
	}

	protected LocalDateTime getLocalDate(final Object object) {
		LocalDateTime localDate = null;
		if (object instanceof LocalDateTime) {
			localDate = (LocalDateTime) object;
		} else if (object instanceof DateTime) {
			DateTime dateTime = (DateTime) object;
			localDate = dateTime.toLocalDateTime();
		}
		return localDate;
	}

	@Override
	public Comparable<?> getComparable(final Object object) {
		LocalDateTime localDate = getLocalDate(object);
		if (object == null) {
			localDate.year().withMinimumValue();
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