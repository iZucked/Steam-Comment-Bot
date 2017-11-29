/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.formatters;

import java.util.List;

import org.eclipse.emf.common.notify.Notifier;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

/**
 */
public class CostFormatter extends IntegerFormatter {

	public enum Type {
	    COST, REVENUE, UNSET 
	}
	
	private final boolean includeUnits;
	private Type type = Type.UNSET;
	
	public CostFormatter(final boolean includeUnits) {
		this.includeUnits = includeUnits;
	}
	
	public CostFormatter(final boolean includeUnits, Type type) {
		this.includeUnits = includeUnits;
		this.type = type;
	}

	public CostFormatter(Type type) {
		this.type = type;
		this.includeUnits = false;
	}
	
	public Integer getIntValue(final Object object) {
		if (object == null) {
			return null;
		}
		return ((Number) object).intValue();
	}
	
	public Type getType() {
		return type;
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
	public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(Object object) {
		return null;
	}
}