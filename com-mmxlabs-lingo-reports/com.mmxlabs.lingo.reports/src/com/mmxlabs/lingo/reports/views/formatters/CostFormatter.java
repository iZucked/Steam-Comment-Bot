/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.formatters;

import java.util.List;

import org.eclipse.emf.common.notify.Notifier;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

/**
 */
public class CostFormatter implements ICellRenderer {

	private final boolean includeUnits;

	public CostFormatter(final boolean includeUnits) {
		this.includeUnits = includeUnits;
	}

	public Integer getIntValue(final Object object) {
		if (object == null) {
			return null;
		}
		return ((Number) object).intValue();
	}

	@Override
	public String render(final Object object) {
		if (object == null) {
			return "";
		}
		final Integer x = getIntValue(object);
		if (x == null) {
			return "";
		}
		return String.format(includeUnits ? "$%,d" : "%,d", x);
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
	public Object getFilterValue(final Object object) {
		return getComparable(object);
	}

	@Override
	public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(Object object) {
		return null;
	}
}