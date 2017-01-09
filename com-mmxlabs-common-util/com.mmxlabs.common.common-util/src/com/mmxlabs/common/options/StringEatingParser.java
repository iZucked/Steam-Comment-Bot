/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.options;

import java.util.Iterator;

public class StringEatingParser implements OptionParser {
	protected String defaultValue;
	protected boolean hasDefaultValue;

	public StringEatingParser(final String defaultValue) {
		this.defaultValue = defaultValue;
		hasDefaultValue = true;
	}

	public StringEatingParser() {
		hasDefaultValue = false;
	}

	@Override
	public Object getDefaultValue() {
		return defaultValue;
	}

	@Override
	public boolean hasDefaultValue() {
		return hasDefaultValue;
	}

	@Override
	public Object parse(final String op, final Iterator<String> iter) throws InvalidArgumentException {
		final StringBuffer sb = new StringBuffer();
		while (iter.hasNext()) {
			sb.append(iter.next());
			sb.append(" ");
		}
		return sb.toString();
	}
}
