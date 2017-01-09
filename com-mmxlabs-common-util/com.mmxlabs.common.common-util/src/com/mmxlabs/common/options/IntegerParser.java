/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.options;

import java.util.Iterator;

public class IntegerParser implements OptionParser {
	protected Integer defaultValue = null;

	public IntegerParser(final Integer defaultValue) {
		this.defaultValue = defaultValue;
	}

	public IntegerParser(final String value) {
		this.defaultValue = Integer.parseInt(value);
	}

	public IntegerParser() {

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
		try {
			final String s = iter.next();
			final Integer i = Integer.valueOf(s);
			return i;
		} catch (final Exception e) {
			throw new InvalidArgumentException(e.getMessage());
		}
	}

}
