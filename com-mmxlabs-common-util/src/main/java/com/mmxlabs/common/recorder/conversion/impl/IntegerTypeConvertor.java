/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.common.recorder.conversion.impl;

import com.mmxlabs.common.recorder.conversion.ITypeConvertor;

public class IntegerTypeConvertor implements ITypeConvertor {

	@Override
	public Class<?> getDataType() {
		return Integer.class;
	}

	@Override
	public Integer toObject(String value) {
		return Integer.parseInt(value);
	}

	@Override
	public String toString(Object object) {
		return Integer.toString((Integer)object);
	}
}
