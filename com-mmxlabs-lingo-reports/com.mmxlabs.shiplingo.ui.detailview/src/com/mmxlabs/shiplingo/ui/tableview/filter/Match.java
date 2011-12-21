/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.tableview.filter;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

import com.mmxlabs.common.Pair;
import com.mmxlabs.shiplingo.importer.importers.DateTimeParser;

/**
 * @author hinton
 * 
 */
class Match implements IFilter {
	public enum Operation {
		LIKE, EQUAL, NOTEQUAL, LESS, GREATER;

		/**
		 * @param matchOp
		 * @return
		 */
		public static Operation fromToken(int matchOp) {
			switch (matchOp) {
			case '=':
				return EQUAL;
			case '~':
				return NOTEQUAL;
			case '<':
				return LESS;
			case '>':
				return GREATER;
			default:
				return LIKE;
			}
		}
	}

	private final Operation operation;
	private final String key;
	private final String value;
	private String lowerValue;
	private Double numberValue;
	private Integer integerValue;

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
		try {
			numberValue = Double.parseDouble(value);
		} catch (NumberFormatException e) {
			numberValue = null;
		}
		try {
			integerValue = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			integerValue = null;
		}
	}

	@Override
	public boolean matches(final Map<String, Pair<?,?>> properties) {
		if (key == null) {
			for (final Pair<?,?> o : properties.values()) {
				if (matches(o.getFirst(),o.getSecond()))
					return true;
			}
			return false;
		} else {
			final Pair<?, ?> value = properties.get(key);
			if (value == null) return false;
			return matches(value.getFirst(), value.getSecond());
		}
	}

	private boolean matches(final Object value, Object renderedValue) {
		switch (operation) {
		case LIKE:
			if (value instanceof Calendar) {
				if (likeCalendar((Calendar) value))
					return true;
				return likeLowercaseString(renderedValue);
			}
			return likeLowercaseString(value);
		case EQUAL:
			if (value instanceof Number) {
				if (numberValue != null) {
					return ((Number) value).doubleValue() == numberValue.doubleValue();
				}
			}
			return ("" + value).equals(this.value);
		case NOTEQUAL: // more NOTLIKE to be honest
			if (value instanceof Number) {
				if (numberValue != null) {
					return ((Number) value).doubleValue() != numberValue.doubleValue();
				}
			}
			return !(("" + value).toLowerCase().contains(lowerValue));
		case GREATER:
			if (value instanceof Number) {
				if (numberValue != null) {
					return ((Number) value).doubleValue() > numberValue.doubleValue();
				}
			} else if (value instanceof Calendar) {

			}
			return ("" + value).toLowerCase().compareTo(lowerValue) >= 0;
		case LESS:
			if (value instanceof Number) {
				if (numberValue != null) {
					return ((Number) value).doubleValue() < numberValue.doubleValue();
				}
			} else if (value instanceof Calendar) {
				// date related stuff
			}
			return ("" + value).toLowerCase().compareTo(lowerValue) <= 0;
		}
		return false;
	}

	/**
	 * @param input
	 * @return
	 */
	private boolean likeLowercaseString(Object input) {
		final String lowerInput = (input + "").toLowerCase();
		return lowerInput.contains(lowerValue) ;
	}

	private final static DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();

	// private final static
	/**
	 * @param value2
	 * @return
	 */
	private boolean likeCalendar(final Calendar calendar) {
		if (integerValue != null) {
			final int i = integerValue;
			return calendar.get(Calendar.YEAR) == integerValue || calendar.get(Calendar.DAY_OF_MONTH) == i || calendar.get(Calendar.MONTH) + 1 == i;
		}

		if (likeLowercaseString(dateFormatSymbols.getMonths()[calendar.get(Calendar.MONTH)]))
			return true;

		return false;
	}

	@Override
	public IFilter collapse() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + (key == null ? "" : key + " ") + operation + " " + value + "]";
	}
}
