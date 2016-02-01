/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.formatters;

import java.util.List;

import org.eclipse.emf.common.notify.Notifier;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

public class IntegerFormatter implements ICellRenderer {
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
		return String.format("%,d", x);
	}

	@Override
	public boolean isValueUnset(Object object) {
		return false;
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
	public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
		return null;
	}
}