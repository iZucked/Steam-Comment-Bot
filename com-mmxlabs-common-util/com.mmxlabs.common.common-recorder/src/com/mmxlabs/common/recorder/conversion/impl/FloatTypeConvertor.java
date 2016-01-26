/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion.impl;

import com.mmxlabs.common.recorder.conversion.ITypeConvertor;

/**
 * Implementation of {@link ITypeConvertor} to convert between a {@link Float} and a {@link String} using {@link Float} conversion methods.
 * 
 * @author Simon Goodall
 * 
 */
public class FloatTypeConvertor implements ITypeConvertor<Float> {

	@Override
	public Class<Float> getDataType() {
		return Float.class;
	}

	@Override
	public Float toObject(final String value) {
		return Float.parseFloat(value);
	}

	@Override
	public String toString(final Object object) {
		return Float.toString((Float) object);
	}
}
