package com.mmxlabs.lingo.reports.views.vertical;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;

import com.mmxlabs.models.lng.schedule.Event;

/**
 * Label provider for the "date" column: provide the date in a specified format.
 * 
 * @author Simon McGregor
 * 
 */
public class DateColumnLabelProvider extends EventLabelProvider {
	private final DateTimeFormatter df;

	public DateColumnLabelProvider(final AbstractVerticalReportVisualiser verticalReportVisualiser) {
		super(verticalReportVisualiser);
		this.df = verticalReportVisualiser.createDateFormat();
	}

	@Override
	protected String getText(final LocalDate date, final Event event) {
		return df.print(date);
	}
}