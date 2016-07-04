/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.properties.ui;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.util.Locale;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;

public class DateTimeFormatLabelProvider extends BaseLabelProvider implements ILabelProvider {

	private final DateTimeFormatter formatter;
	private final boolean showZone;

	public DateTimeFormatLabelProvider(final boolean showZone) {
		this(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT), showZone);
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
		if (element instanceof ZonedDateTime) {
			final ZonedDateTime dateTime = (ZonedDateTime) element;
			String timeStr = dateTime.format(formatter);
			if (showZone) {
				return String.format("%s (%s)", timeStr, dateTime.getZone().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
			} else {
				return timeStr;
			}
		} else {
			throw new IllegalArgumentException("Can only format DateTimes!");
		}
	}
}
