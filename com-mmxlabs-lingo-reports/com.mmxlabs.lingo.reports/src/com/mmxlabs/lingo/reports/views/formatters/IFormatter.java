/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.formatters;

import com.mmxlabs.models.ui.tabular.IComparableProvider;

/**
 */
public interface IFormatter extends IComparableProvider {
	public String format(final Object object);

	public Comparable getComparable(final Object object);

	public Object getFilterable(final Object object);
}