/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.formatters;

import java.text.DateFormat;

import org.joda.time.format.DateTimeFormat;

import com.mmxlabs.models.ui.tabular.ICellRenderer;

public final class Formatters {

	public static final ICellRenderer objectFormatter = new BaseFormatter();

	public static final ICellRenderer asLocalDateFormatter = new AsLocalDateFormatter(DateFormat.getDateInstance(DateFormat.SHORT));
	public static final ICellRenderer asLocalTimeFormatter = new AsLocalDateFormatter(DateFormat.getTimeInstance(DateFormat.SHORT));

	public static final ICellRenderer asDateTimeFormatterWithTZ = new AsDateTimeFormatter(DateTimeFormat.shortDateTime(), true);
	public static final ICellRenderer asDateTimeFormatterNoTz = new AsDateTimeFormatter(DateTimeFormat.shortDateTime(), false);

	public static final IntegerFormatter integerFormatter = new IntegerFormatter();

}
