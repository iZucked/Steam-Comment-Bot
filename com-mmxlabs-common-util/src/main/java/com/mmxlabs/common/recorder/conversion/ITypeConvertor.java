/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion;

public interface ITypeConvertor {

	Class<?> getDataType();

	Object toObject(String value);

	String toString(Object object);
}
