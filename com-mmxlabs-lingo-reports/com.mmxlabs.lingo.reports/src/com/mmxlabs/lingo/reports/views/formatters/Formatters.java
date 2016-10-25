/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.formatters;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.ui.tabular.BaseFormatter;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

public final class Formatters {

	public static final ICellRenderer objectFormatter = new BaseFormatter();

	public static final ICellRenderer asLocalDateFormatter = new AsLocalDateFormatter(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
	public static final ICellRenderer asLocalTimeFormatter = new AsLocalDateFormatter(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));

	public static final ICellRenderer asDateTimeFormatterWithTZ = new AsDateTimeFormatter(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT), true);
	public static final ICellRenderer asDateTimeFormatterNoTz = new AsDateTimeFormatter(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT), false);

	public static final IntegerFormatter integerFormatter = new IntegerFormatter();

	public static @NonNull String formatAsDays(int hours) {
		return String.format("%02d:%02d", hours / 24, hours % 24);
	}
}
