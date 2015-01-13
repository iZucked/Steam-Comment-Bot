/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.formatters;

import java.text.DateFormat;

import com.mmxlabs.models.ui.tabular.ICellRenderer;

public final class Formatters {

	public static final ICellRenderer objectFormatter = new BaseFormatter();

	public static final ICellRenderer calendarFormatter = new CalendarFormatter(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT), true);
	public static final ICellRenderer calendarFormatterNoTZ = new CalendarFormatter(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT), false);

	public static final ICellRenderer datePartFormatter = new CalendarFormatter(DateFormat.getDateInstance(DateFormat.SHORT), false);
	public static final ICellRenderer timePartFormatter = new CalendarFormatter(DateFormat.getTimeInstance(DateFormat.SHORT), false);

	public static final IntegerFormatter integerFormatter = new IntegerFormatter();

}
