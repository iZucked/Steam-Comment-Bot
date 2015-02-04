/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical.labellers;

import org.eclipse.jdt.annotation.NonNull;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;

import com.mmxlabs.lingo.reports.views.vertical.AbstractVerticalReportVisualiser;
import com.mmxlabs.models.lng.schedule.Event;

/**
 * Label provider for the "date" column: provide the date in a specified format.
 * 
 * @author Simon McGregor
 * 
 */
public class DateColumnLabelProvider extends EventLabelProvider {
	private final DateTimeFormatter df;

	public DateColumnLabelProvider(@NonNull final AbstractVerticalReportVisualiser verticalReportVisualiser) {
		super(verticalReportVisualiser);
		this.df = verticalReportVisualiser.createDateFormat();
	}

	@Override
	protected String getText(@NonNull final LocalDate date, @NonNull final Event event) {
		return df.print(date);
	}
}