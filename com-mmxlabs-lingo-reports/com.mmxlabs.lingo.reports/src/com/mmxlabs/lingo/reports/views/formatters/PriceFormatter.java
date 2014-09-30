/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.formatters;

/**
 */
public class PriceFormatter implements IFormatter {

	private final String formatString;

	public PriceFormatter(final boolean includeUnits, int dp) {
		this.formatString = (includeUnits ? "$" : "") + "%,." + Integer.toString(dp) + "f";
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
		return String.format(formatString, x);
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