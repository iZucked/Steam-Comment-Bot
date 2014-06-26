package com.mmxlabs.lingo.reports.views.formatters;

/**
 */
public interface IFormatter {
	public String format(final Object object);

	public Comparable getComparable(final Object object);

	public Object getFilterable(final Object object);
}