package com.mmxlabs.lingo.reports.views.vertical;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.joda.time.format.DateTimeFormatter;

import com.mmxlabs.lingo.reports.views.vertical.AbstractVerticalCalendarReportView.ReportNebulaGridManager;

public class CalendarColumn {
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
		return new CalendarColumnLabelProvider(df, provider, labeller, manager);
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