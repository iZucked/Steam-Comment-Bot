/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import java.util.List;

import org.eclipse.emf.common.notify.Notifier;

import com.mmxlabs.common.Pair;

public class BaseFormatter implements ICellRenderer {
	@Override
	public String render(final Object object) {
		if (object == null) {
			return "";
		} else {
			return object.toString();
		}
	}

	@Override
	public boolean isValueUnset(Object object) {
		return false;
	}

	@Override
	public Comparable getComparable(final Object object) {
		return render(object);
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