/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.common.options;


import java.util.Iterator;

public class StringParser implements OptionParser {
	String defaultValue = null;
	public StringParser() {	}
	public StringParser(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	@Override
	public Object parse(String op, Iterator<String> iter) throws InvalidArgumentException {
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
