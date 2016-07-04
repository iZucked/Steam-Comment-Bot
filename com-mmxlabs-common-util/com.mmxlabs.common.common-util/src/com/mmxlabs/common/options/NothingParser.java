/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.options;

import java.util.Iterator;

public class NothingParser implements OptionParser {
	@Override
	public Object parse(final String op, final Iterator<String> iter) throws InvalidArgumentException {
		return true;
	}

	@Override
	public Object getDefaultValue() {
		return false;
	}

	@Override
	public boolean hasDefaultValue() {

		return true;
	}
}
