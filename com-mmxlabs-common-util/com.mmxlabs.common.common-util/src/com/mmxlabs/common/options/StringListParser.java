/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.options;

import java.util.Arrays;
import java.util.Iterator;

public class StringListParser implements OptionParser {
	protected String separator;
	protected String defaultValue;

	public StringListParser(final String separator, final String defaultValue) {
		this.defaultValue = defaultValue;
		this.separator = separator;
	}

	public StringListParser(final String separator) {
		this(separator, null);
	}

	@Override
	public Object parse(final String op, final Iterator<String> iter) throws InvalidArgumentException {
		final String s = iter.next();
		final String[] args = s.split(separator);
		return Arrays.asList(args);
	}

	@Override
	public Object getDefaultValue() {
		if (defaultValue != null) {
			return Arrays.asList(defaultValue.split(separator));
		} else {
			return null;
		}
	}

	@Override
	public boolean hasDefaultValue() {
		return defaultValue != null;
	}
}
