/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.ui.properties.ui;

import java.util.TimeZone;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeFormatLabelProvider extends BaseLabelProvider implements ILabelProvider {

	private final DateTimeFormatter formatter;
	private final boolean showZone;

	public DateTimeFormatLabelProvider(final boolean showZone) {
		this(DateTimeFormat.shortDateTime(), showZone);
	}

	public DateTimeFormatLabelProvider(final DateTimeFormatter formatter, final boolean showZone) {
		this.showZone = showZone;
		this.formatter = formatter;
	}

	@Override
	public Image getImage(final Object element) {
		return null;
	}

	@Override
	public String getText(final Object element) {
		if (element instanceof DateTime) {
			final DateTime dateTime = (DateTime) element;
			return formatter.print(dateTime) + (showZone ? (" (" + dateTime.getZone().toTimeZone().getDisplayName(false, TimeZone.SHORT) + ")") : "");
		} else {
			throw new IllegalArgumentException("Can only format DateTimes!");
		}
	}
}
