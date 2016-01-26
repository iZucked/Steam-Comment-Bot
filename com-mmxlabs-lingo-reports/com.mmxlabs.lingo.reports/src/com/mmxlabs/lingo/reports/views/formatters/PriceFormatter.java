/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.formatters;

import java.util.List;

import org.eclipse.emf.common.notify.Notifier;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

/**
 */
public class PriceFormatter implements ICellRenderer {

	private final String formatString;

	public PriceFormatter(final boolean includeUnits, final int dp) {
		this.formatString = (includeUnits ? "$" : "") + "%,." + Integer.toString(dp) + "f";
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
	public Object getFilterValue(final Object object) {
		return getComparable(object);
	}

	@Override
	public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
		return null;
	}
}