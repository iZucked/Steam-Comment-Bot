/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.options;

import java.util.Iterator;

public class DoubleParser implements OptionParser {
	private Double defaultValue = null;

	public DoubleParser() {
	}

	public DoubleParser(final String value) {
		defaultValue = Double.parseDouble(value);
	}

	public DoubleParser(final double value) {
		this.defaultValue = value;
	}

	@Override
	public Object getDefaultValue() {
		return defaultValue;
	}

	@Override
	public boolean hasDefaultValue() {
		return defaultValue != null;
	}

	@Override
	public Object parse(final String op, final Iterator<String> iter) throws InvalidArgumentException {
		final String s = iter.next();
		try {
			return Double.parseDouble(s);
		} catch (final Exception e) {
			throw new InvalidArgumentException("Are you sure " + s + " is a double?");
		}
	}

}
