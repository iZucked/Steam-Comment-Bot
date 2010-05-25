package com.mmxlabs.common.recorder.conversion.impl;

import com.mmxlabs.common.recorder.conversion.ITypeConvertor;

public class DoubleTypeConvertor implements ITypeConvertor {

	@Override
	public Class<?> getDataType() {
		return Double.class;
	}

	@Override
	public Double toObject(String value) {
		return Double.parseDouble(value);
	}

	@Override
	public String toString(Object object) {
		return Double.toString((Double) object);
	}
}
