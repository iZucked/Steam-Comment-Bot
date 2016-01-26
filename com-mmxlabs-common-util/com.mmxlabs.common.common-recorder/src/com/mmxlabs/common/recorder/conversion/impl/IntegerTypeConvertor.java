/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion.impl;

import com.mmxlabs.common.recorder.conversion.ITypeConvertor;

/**
 * Implementation of {@link ITypeConvertor} to convert between a {@link Integer} and a {@link String} using {@link Integer} conversion methods.
 * 
 * @author Simon Goodall
 * 
 */
public class IntegerTypeConvertor implements ITypeConvertor<Integer> {

	@Override
	public Class<Integer> getDataType() {
		return Integer.class;
	}

	@Override
	public Integer toObject(final String value) {
		return Integer.parseInt(value);
	}

	@Override
	public String toString(final Object object) {
		return Integer.toString((Integer) object);
	}
}
