package com.mmxlabs.lingo.reports.views.formatters;

import java.text.DateFormat;

public final class Formatters {

	public static final IFormatter objectFormatter = new BaseFormatter();

	public static final IFormatter calendarFormatter = new CalendarFormatter(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT), true);
	public static final IFormatter calendarFormatterNoTZ = new CalendarFormatter(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT), false);

	public static final IFormatter datePartFormatter = new CalendarFormatter(DateFormat.getDateInstance(DateFormat.SHORT), false);
	public static final IFormatter timePartFormatter = new CalendarFormatter(DateFormat.getTimeInstance(DateFormat.SHORT), false);

	public static final IntegerFormatter integerFormatter = new IntegerFormatter();

}
