/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.tableview.filter;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.mmxlabs.shiplingo.ui.tableview.filter.Match.Operation;

/**
 * @author hinton
 *
 */
class Match implements IFilter {
	public enum Operation {
		LIKE,
		EQUAL,
		NOTEQUAL,
		LESS,
		GREATER;

		/**
		 * @param matchOp
		 * @return
		 */
		public static Operation fromToken(int matchOp) {
			switch (matchOp) {
			case '=':return EQUAL;
			case '~':return NOTEQUAL;
			case '<':return LESS;
			case '>':return GREATER;
			default:return LIKE;
			}
		}
	}
	private final Operation operation;
	private final String key;
	private final String value;
	private String lowerValue;
	
	public Match(final String value) {
		this(Operation.LIKE, value);
	}
	
	public Match(final Operation operation, final String value) {
		this(operation, null, value);
	}
	
	public Match(final Operation operation, final String key, final String value) {
		this.operation = operation;
		this.key = key;
		this.value = value;
		this.lowerValue = value.toLowerCase();
	}
	
	
	@Override
	public boolean matches(final Map<String, ?> properties) {
		if (key == null) {
			for (final Object o : properties.values()) {
				if (matches(o)) return true;
			}
			return false;
		} else {
			return matches(properties.get(key));
		}
	}

	private boolean matches(final Object value) {
		switch (operation) {
		case LIKE:
			return ("" + value).toLowerCase().contains(lowerValue);
		case EQUAL:
			return (""+value).equals(this.value);
		}
		return false;
	}

	@Override
	public IFilter collapse() {
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "["+(key == null ? "" : key + " ")+operation + " "+value+"]";
	}
}
