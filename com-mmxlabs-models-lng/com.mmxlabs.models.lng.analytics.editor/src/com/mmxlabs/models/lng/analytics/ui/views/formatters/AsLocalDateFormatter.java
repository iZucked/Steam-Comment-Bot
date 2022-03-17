/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.formatters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.MTMRow;
import com.mmxlabs.models.lng.cargo.LoadSlot;
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
		if (object instanceof MTMRow) {
			final MTMRow row = (MTMRow) object;
			final BuyOption buy = row.getBuyOption();
			return (render(buy));
		}
		if (object instanceof BuyReference) {
			final BuyReference buyReference = (BuyReference) object;
			final LoadSlot slot = buyReference.getSlot();
			return render(slot.getWindowStart());
		}
		if (object instanceof LocalDate) {
			LocalDate localDate = (LocalDate)object;
			return localDate.format(dateFormat);
		}
		final LocalDateTime localDate = getLocalDate(object);
		if (localDate != null) {
			return localDate.format(dateFormat);
		}
		return null;
	}

	protected LocalDateTime getLocalDate(final Object object) {
		LocalDateTime localDate = null;
		if (object instanceof MTMRow) {
			final MTMRow row = (MTMRow) object;
			final BuyOption buy = row.getBuyOption();
			return (getLocalDate(buy));
		}
		if (object instanceof BuyReference) {
			final BuyReference buyReference = (BuyReference) object;
			final LoadSlot slot = buyReference.getSlot();
			return getLocalDate(slot.getWindowStart());
		}
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
		final LocalDateTime localDate = getLocalDate(object);
		if (object == null) {
			return LocalDateTime.now().withYear(Year.MIN_VALUE);
		}
		return localDate;
	}

	@Override
	public Object getFilterValue(final Object object) {
		final LocalDateTime localDate = getLocalDate(object);
		if (localDate != null) {
			return localDate;
		}
		return object;
	}
}