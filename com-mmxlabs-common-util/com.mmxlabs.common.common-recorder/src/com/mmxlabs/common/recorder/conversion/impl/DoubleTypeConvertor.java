/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion.impl;

import com.mmxlabs.common.recorder.conversion.ITypeConvertor;

/**
 * Implementation of {@link ITypeConvertor} to convert between a {@link Double} and a {@link String} using {@link Double} conversion methods.
 * 
 * @author Simon Goodall
 * 
 */
public class DoubleTypeConvertor implements ITypeConvertor<Double> {

	@Override
	public Class<Double> getDataType() {
		return Double.class;
	}

	@Override
	public Double toObject(final String value) {
		return Double.parseDouble(value);
	}

	@Override
	public String toString(final Object object) {
		return Double.toString((Double) object);
	}
}
