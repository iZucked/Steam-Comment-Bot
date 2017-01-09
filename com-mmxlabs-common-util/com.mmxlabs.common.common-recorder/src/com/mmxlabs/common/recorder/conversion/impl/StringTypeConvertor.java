/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion.impl;

import com.mmxlabs.common.recorder.conversion.ITypeConvertor;

/**
 * Implementation of {@link ITypeConvertor} to perform a null conversion for {@link String} objects.
 * 
 * @author Simon Goodall
 * 
 */
public class StringTypeConvertor implements ITypeConvertor<String> {

	@Override
	public Class<String> getDataType() {
		return String.class;
	}

	@Override
	public String toObject(final String value) {
		return value;
	}

	@Override
	public String toString(final Object object) {
		return (String) object;
	}
}
