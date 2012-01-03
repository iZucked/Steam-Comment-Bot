/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.common.options;


import java.util.Iterator;

public class StringEatingParser implements OptionParser {
	protected String defaultValue;
	protected boolean hasDefaultValue;
	public StringEatingParser(String defaultValue) {
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
	public Object parse(String op, Iterator<String> iter)
			throws InvalidArgumentException {
		StringBuffer sb = new StringBuffer();
		while (iter.hasNext()) {
			sb.append(iter.next());
			sb.append(" ");
		}
		return sb.toString();
	}
}
