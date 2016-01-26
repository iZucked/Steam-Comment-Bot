/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion.impl;

import com.mmxlabs.common.recorder.conversion.ITypeConvertor;

/**
 * Implementation of {@link ITypeConvertor} to convert between a {@link Long} and a {@link String} using {@link Long} conversion methods.
 * 
 * @author Simon Goodall
 * 
 */
public class LongTypeConvertor implements ITypeConvertor<Long> {

	@Override
	public Class<Long> getDataType() {
		return Long.class;
	}

	@Override
	public Long toObject(final String value) {
		return Long.parseLong(value);
	}

	@Override
	public String toString(final Object object) {
		return Long.toString((Long) object);
	}
}
