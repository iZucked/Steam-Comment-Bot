/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical;

import java.time.format.DateTimeFormatter;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
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
	private final @NonNull EventProvider provider;
	private final @NonNull EventLabelProvider labeller;
	private final @Nullable String title;
	private final @Nullable GridColumnGroup columnGroup;
	private final @NonNull DateTimeFormatter df;

	public CalendarColumn(final @NonNull DateTimeFormatter df, final @NonNull EventProvider provider, final @NonNull EventLabelProvider labeller, final @Nullable String title,
			final @Nullable GridColumnGroup columnGroup) {
		this.df = df;
		this.provider = provider;
		this.labeller = labeller;
		this.title = title;
		this.columnGroup = columnGroup;
	}

	public ColumnLabelProvider createColumnLabelProvider(final @NonNull ReportNebulaGridManager manager) {
		return new LocalDateColumnLabelProvider(df, provider, labeller, manager);
	}

	public @Nullable String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		final String pTitle = title;
		return (pTitle == null) ? "(null)" : pTitle;
	}

	public @Nullable GridColumnGroup getColumnGroup() {
		return columnGroup;
	}

	public EventProvider getProvider() {
		return provider;
	}

	public EventLabelProvider getLabeller() {
		return labeller;
	}
}