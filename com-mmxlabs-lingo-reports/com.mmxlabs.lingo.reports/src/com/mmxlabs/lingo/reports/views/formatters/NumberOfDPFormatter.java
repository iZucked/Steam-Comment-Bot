/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.formatters;

import com.google.common.base.Preconditions;

/**
 * Formatter to format a floating point number to a given number of decimal places.
 * 
 * @author Simon Goodall
 * 
 */
public class NumberOfDPFormatter implements IFormatter {

	private final int dp;

	public NumberOfDPFormatter(final int dp) {
		Preconditions.checkArgument(dp >= 0);
		this.dp = dp;
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
		return String.format("%,." + dp + "f", x);
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