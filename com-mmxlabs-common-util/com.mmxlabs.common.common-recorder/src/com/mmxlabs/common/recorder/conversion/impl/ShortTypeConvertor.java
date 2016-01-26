/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion.impl;

import com.mmxlabs.common.recorder.conversion.ITypeConvertor;

/**
 * Implementation of {@link ITypeConvertor} to convert between a {@link Short} and a {@link String} using {@link Short} conversion methods.
 * 
 * @author Simon Goodall
 * 
 */
public class ShortTypeConvertor implements ITypeConvertor<Short> {

	@Override
	public Class<Short> getDataType() {
		return Short.class;
	}

	@Override
	public Short toObject(final String value) {
		return Short.parseShort(value);
	}

	@Override
	public String toString(final Object object) {
		return Short.toString((Short) object);
	}
}
