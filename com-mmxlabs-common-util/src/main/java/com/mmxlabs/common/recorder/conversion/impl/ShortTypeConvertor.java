/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion.impl;

import com.mmxlabs.common.recorder.conversion.ITypeConvertor;

public class ShortTypeConvertor implements ITypeConvertor {

	@Override
	public Class<?> getDataType() {
		return Short.class;
	}

	@Override
	public Short toObject(String value) {
		return Short.parseShort(value);
	}

	@Override
	public String toString(Object object) {
		return Short.toString((Short) object);
	}
}
