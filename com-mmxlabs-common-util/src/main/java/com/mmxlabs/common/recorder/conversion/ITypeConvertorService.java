/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion;

public interface ITypeConvertorService {

	public void registerTypeConvertor(final ITypeConvertor convertor);

	public void unregisterTypeConvertor(final String className);

	public Object toObject(final String className, final String value);

	public String toString(final String className, final Object object);

}