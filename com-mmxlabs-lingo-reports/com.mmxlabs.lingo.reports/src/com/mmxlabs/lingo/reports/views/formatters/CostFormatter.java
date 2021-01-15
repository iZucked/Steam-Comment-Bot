/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.formatters;

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
}