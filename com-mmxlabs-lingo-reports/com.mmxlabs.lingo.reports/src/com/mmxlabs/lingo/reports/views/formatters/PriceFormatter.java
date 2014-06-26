package com.mmxlabs.lingo.reports.views.formatters;


/**
 */
public class PriceFormatter implements IFormatter {

	private final boolean includeUnits;

	public PriceFormatter(final boolean includeUnits) {
		this.includeUnits = includeUnits;
	}

	public Double getDoubleValue(final Object object) {
		if (object == null) {
			return null;
		}
		return ((Number) object).doubleValue();
	}

	@Override
	public String format(final Object object) {
		if (object == null) {
			return "";
		}
		final Double x = getDoubleValue(object);
		if (x == null) {
			return "";
		}
		return String.format(includeUnits ? "$%,.2f" : "%,.2f", x);
	}

	@Override
	public Comparable getComparable(final Object object) {
		final Double x = getDoubleValue(object);
		if (x == null) {
			return -Double.MAX_VALUE;
		}
		return x;
	}

	@Override
	public Object getFilterable(final Object object) {
		return getComparable(object);
	}
}