/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.filter;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;

/**
 * @author hinton
 * 
 */
class Match implements IFilter {
	public enum Operation {
		LIKE, EQUAL, NOTEQUAL, LESS, GREATER;

		public static Operation fromToken(final int matchOp) {
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
	private final String lowerValue;
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
		} catch (final NumberFormatException e) {
			numberValue = null;
		}
		try {
			integerValue = Integer.parseInt(value);
		} catch (final NumberFormatException e) {
			integerValue = null;
		}
	}

	@Override
	public boolean matches(final Map<String, Pair<?, ?>> properties) {
		if (key == null) {
			for (final Pair<?, ?> o : properties.values()) {
				if (matches(o.getFirst(), o.getSecond())) {
					return true;
				}
			}
			return false;
		} else {
			final Pair<?, ?> value = properties.get(key);
			if (value == null) {
				return false;
			}
			return matches(value.getFirst(), value.getSecond());
		}
	}

	private boolean matches(final Object value, final Object renderedValue) {
		switch (operation) {
		case LIKE:
			if (value instanceof Calendar) {
				if (likeCalendar((Calendar) value)) {
					return true;
				}
				return likeLowercaseString(renderedValue);
			}
			if (value instanceof ZonedDateTime) {
				if (likeDateTime((ZonedDateTime) value)) {
					return true;
				}
				return likeLowercaseString(renderedValue);
			}
			if (value instanceof LocalDateTime) {
				if (likeLocalDateTime((LocalDateTime) value)) {
					return true;
				}
				return likeLowercaseString(renderedValue);
			}
			if (value instanceof LocalDate) {
				if (likeLocalDate((LocalDate) value)) {
					return true;
				}
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
			return !likeLowercaseString(value);
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
	private boolean likeLowercaseString(final Object input) {
		final String lowerInput = (input + "").toLowerCase();
		return lowerInput.contains(lowerValue);
	}

	private final static DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();

	/**
	 * @param value2
	 * @return
	 */
	private boolean likeCalendar(@NonNull final Calendar calendar) {
		if (integerValue != null) {
			final int i = integerValue;
			return (calendar.get(Calendar.YEAR) == integerValue) || (calendar.get(Calendar.DAY_OF_MONTH) == i) || ((calendar.get(Calendar.MONTH) + 1) == i);
		}

		if (likeLowercaseString(dateFormatSymbols.getMonths()[calendar.get(Calendar.MONTH)])) {
			return true;
		}

		return false;
	}

	private boolean likeDateTime(@NonNull final ZonedDateTime calendar) {
		if (integerValue != null) {
			final int i = integerValue;
			return (calendar.getYear() == integerValue) || (calendar.getDayOfMonth() == i) || (calendar.getMonthValue() == i);
		}

		if (likeLowercaseString(dateFormatSymbols.getMonths()[calendar.getMonthValue() - 1])) {
			return true;
		}

		return false;
	}

	private boolean likeLocalDateTime(@NonNull final LocalDateTime calendar) {
		if (integerValue != null) {
			final int i = integerValue;
			return (calendar.getYear() == integerValue) || (calendar.getDayOfMonth() == i) || (calendar.getMonthValue() == i);
		}

		if (likeLowercaseString(dateFormatSymbols.getMonths()[calendar.getMonthValue() - 1])) {
			return true;
		}

		return false;
	}

	private boolean likeLocalDate(@NonNull final LocalDate calendar) {
		if (integerValue != null) {
			final int i = integerValue;
			return (calendar.getYear() == integerValue) || (calendar.getDayOfMonth() == i) || (calendar.getMonthValue() == i);
		}

		if (likeLowercaseString(dateFormatSymbols.getMonths()[calendar.getMonthValue() - 1])) {
			return true;
		}

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
