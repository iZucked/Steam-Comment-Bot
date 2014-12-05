package com.mmxlabs.lingo.reports.views.vertical;

import java.text.DateFormat;

import org.eclipse.jface.viewers.ColumnLabelProvider;

/**
 * Label provider for the "date" column: provide the date in a specified format.
 * 
 * @author Simon McGregor
 * 
 */
public class DateColumnLabelProvider extends ColumnLabelProvider {
	private final DateFormat df;

	public DateColumnLabelProvider(final DateFormat df) {
		this.df = df;
	}

	@Override
	public String getText(final Object element) {
		return df.format(element);
	}

}