/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.filter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mmxlabs.common.Pair;

/**
 * Matches a conjunction or disjunction of things
 * 
 * @author hinton
 * 
 */
class Group implements IFilter {
	final boolean isConjunction;
	List<IFilter> filters = new LinkedList<IFilter>();

	public Group(final boolean isConjunction) {
		super();
		this.isConjunction = isConjunction;
	}

	@Override
	public boolean matches(final Map<String, Pair<?, ?>> properties) {
		for (final IFilter filter : filters) {
			final boolean match = filter.matches(properties);
			if (isConjunction && !match) {
				return false;
			}
			if (!isConjunction && match) {
				return true;
			}
		}
		return isConjunction;
	}

	public void addFilter(final IFilter element) {
		filters.add(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.shiplingo.ui.tableview.filter.IFilter#collapse()
	 */
	@Override
	public IFilter collapse() {
		final List<IFilter> collapsedFilters = new LinkedList<IFilter>();
		for (IFilter f : filters) {
			if (f != null) {
				f = f.collapse();
			}
			if (f != null) {
				collapsedFilters.add(f);
			}
		}
		filters = collapsedFilters;
		if (filters.size() == 0) {
			return null;
		}
		if (filters.size() == 1) {
			return filters.get(0).collapse();
		} else {
			return this;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + (isConjunction ? "AND" : "OR") + " " + filters + "]";
	}
}
