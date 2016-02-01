/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical;

import java.time.format.DateTimeFormatter;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;

import com.mmxlabs.lingo.reports.views.vertical.labellers.EventLabelProvider;
import com.mmxlabs.lingo.reports.views.vertical.labellers.LocalDateColumnLabelProvider;
import com.mmxlabs.lingo.reports.views.vertical.providers.EventProvider;

/**
 * The calendar column is a class to describe a particular column in the {@link AbstractVerticalCalendarReportView} and is used to generate Nebula {@link Grid} objects from previously created
 * {@link EventProvider} and {@link EventLabelProvider} implementations.
 * 
 * @author Simon McGregor
 *
 */
public final class CalendarColumn {
	private final EventProvider provider;
	private final EventLabelProvider labeller;
	private final String title;
	private final GridColumnGroup columnGroup;
	private final DateTimeFormatter df;

	public CalendarColumn(final DateTimeFormatter df, final EventProvider provider, final EventLabelProvider labeller, final String title, final GridColumnGroup columnGroup) {
		this.df = df;
		this.provider = provider;
		this.labeller = labeller;
		this.title = title;
		this.columnGroup = columnGroup;
	}

	public ColumnLabelProvider createColumnLabelProvider(final ReportNebulaGridManager manager) {
		return new LocalDateColumnLabelProvider(df, provider, labeller, manager);
	}

	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return (title == null) ? "(null)" : title;
	}

	public GridColumnGroup getColumnGroup() {
		return columnGroup;
	}

	public EventProvider getProvider() {
		return provider;
	}

	public EventLabelProvider getLabeller() {
		return labeller;
	}
}