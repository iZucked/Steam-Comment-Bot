/**
 * Copyright (C) Minimaxlabs, 2010
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
	
	public Object parse(String op, Iterator<String> iter) throws InvalidArgumentException {
		return iter.next();
	}
	public Object getDefaultValue() {
		return this.defaultValue;
	}
	public boolean hasDefaultValue() {
		return (this.defaultValue != null);
	}

}
