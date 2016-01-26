/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.formatters;

import java.util.List;

import org.eclipse.emf.common.notify.Notifier;

import com.google.common.base.Preconditions;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

/**
 * Formatter to format a floating point number to a given number of decimal places.
 * 
 * @author Simon Goodall
 * 
 */
public class NumberOfDPFormatter implements ICellRenderer {

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
	public boolean isValueUnset(final Object object) {
		return false;
	}

	@Override
	public String render(final Object object) {
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
	public Object getFilterValue(final Object object) {
		return getComparable(object);
	}

	@Override
	public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
		return null;
	}
}