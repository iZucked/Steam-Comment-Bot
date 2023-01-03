/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;

public abstract class SimpleCellRenderer implements ICellRenderer {

	@Override
	public Comparable getComparable(Object object) {
		return render(object);
	}

	@Override
	public @Nullable Object getFilterValue(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValueUnset(Object object) {
		return false;
	}

	@Override
	public @Nullable Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(Object object) {
		return null;
	}

}
