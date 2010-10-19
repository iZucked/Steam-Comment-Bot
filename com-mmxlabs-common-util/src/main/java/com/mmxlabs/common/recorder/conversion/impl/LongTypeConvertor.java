/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.common.recorder.conversion.impl;

import com.mmxlabs.common.recorder.conversion.ITypeConvertor;

public class LongTypeConvertor implements ITypeConvertor {

	@Override
	public Class<?> getDataType() {
		return Long.class;
	}

	@Override
	public Long toObject(String value) {
		return Long.parseLong(value);
	}

	@Override
	public String toString(Object object) {
		return Long.toString((Long) object);
	}
}
