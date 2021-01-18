/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.optimiser.exporter.util;

public interface Parameter<T> {
	public String getKey();
	public T getValue();
	public void setValue(Object value);
	public T getDefaultValue();
	public boolean isOfType(Class<?> c);
}
