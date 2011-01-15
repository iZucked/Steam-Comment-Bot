/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion.impl;

import com.mmxlabs.common.recorder.conversion.ITypeConvertor;

public class FloatTypeConvertor implements ITypeConvertor {

	@Override
	public Class<?> getDataType() {
		return Float.class;
	}

	@Override
	public Float toObject(String value) {
		return Float.parseFloat(value);
	}

	@Override
	public String toString(Object object) {
		return Float.toString((Float) object);
	}
}
