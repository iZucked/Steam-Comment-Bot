/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.optimiser.exporter.util;

public abstract class AParameter<T> implements Parameter<T> {
	protected String key;
	protected T value;
	protected T defaultValue;
	protected Class<T> parameterClass;

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public T getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void setValue(Object o) {
		if (isOfType(o.getClass())) {
			this.value = cast(o);
		}
	}

	@Override
	public T getValue() {
		if (value == null) {
			return defaultValue;
		} else {
			return value;
		}
	}

	@Override
	public boolean isOfType(Class<?> clazz) {
		return parameterClass.isAssignableFrom(clazz);
	}

	public T cast(Object o) {
		return parameterClass.cast(o);
	}
}
