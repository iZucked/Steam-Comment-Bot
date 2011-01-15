/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.common.options;


import java.util.Iterator;

public class NothingParser implements OptionParser {
	@Override
	public Object parse(String op, Iterator<String> iter) throws InvalidArgumentException {
		return true;
	}

	@Override
	public Object getDefaultValue() {
		return false;
	}

	@Override
	public boolean hasDefaultValue() {
		// TODO Auto-generated method stub
		return true;
	}
}
