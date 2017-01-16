/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless.utils;

public interface Parameter<T> {
	public String getKey();
	public T getValue();
	public void setValue(Object value);
	public T getDefaultValue();
	public boolean isOfType(Class<?> c);
}
