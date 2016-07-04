/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion;

import com.mmxlabs.common.ITransformer;

/**
 * Interface defining a two-way conversion mechanism between an object and a {@link String}. This differs to the {@link ITransformer} interface which only provides a one-way conversion.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Object data type
 */
public interface ITypeConvertor<T> {

	Class<T> getDataType();

	T toObject(String value);

	String toString(Object object);
}
