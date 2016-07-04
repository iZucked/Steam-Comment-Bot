/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.options;

import java.util.Iterator;

public class StringParser implements OptionParser {
	String defaultValue = null;

	public StringParser() {
	}

	public StringParser(final String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public Object parse(final String op, final Iterator<String> iter) throws InvalidArgumentException {
		return iter.next();
	}

	@Override
	public Object getDefaultValue() {
		return this.defaultValue;
	}

	@Override
	public boolean hasDefaultValue() {
		return (this.defaultValue != null);
	}

}
