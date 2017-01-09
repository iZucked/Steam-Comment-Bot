/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless.utils;

public abstract class AParameter<T> implements Parameter<T> {
	public String key;
	public T value;
	public T defaultValue;
	public Class<T> parameterClass;
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
