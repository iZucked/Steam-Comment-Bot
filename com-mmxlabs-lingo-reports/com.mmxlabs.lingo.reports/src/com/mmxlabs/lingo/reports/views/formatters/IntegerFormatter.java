package com.mmxlabs.lingo.reports.views.formatters;


public class IntegerFormatter implements IFormatter {
	public Integer getIntValue(final Object object) {
		if (object == null) {
			return null;
		}
		return ((Number) object).intValue();
	}

	@Override
	public String format(final Object object) {
		if (object == null) {
			return "";
		}
		final Integer x = getIntValue(object);
		if (x == null) {
			return "";
		}
		return String.format("%,d", x);
	}

	@Override
	public Comparable getComparable(final Object object) {
		final Integer x = getIntValue(object);
		if (x == null) {
			return -Integer.MAX_VALUE;
		}
		return x;
	}

	@Override
	public Object getFilterable(final Object object) {
		return getComparable(object);
	}
}