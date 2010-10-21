/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.common.recorder.conversion.impl;

import com.mmxlabs.common.recorder.conversion.ITypeConvertor;

public class StringTypeConvertor implements ITypeConvertor {

	@Override
	public Class<?> getDataType() {
		return String.class;
	}

	@Override
	public String toObject(String value) {
		return value;
	}

	@Override
	public String toString(Object object) {
		return (String)object;
	}
}
