package com.mmxlabs.lingo.reports.views.formatters;


public class BaseFormatter implements IFormatter {
	@Override
	public String format(final Object object) {
		if (object == null) {
			return "";
		} else {
			return object.toString();
		}
	}

	@Override
	public Comparable getComparable(final Object object) {
		return format(object);
	}

	@Override
	public Object getFilterable(final Object object) {
		return getComparable(object);
	}
}