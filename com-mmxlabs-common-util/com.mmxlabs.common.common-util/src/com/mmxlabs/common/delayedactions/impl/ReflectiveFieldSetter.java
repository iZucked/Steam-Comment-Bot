/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.delayedactions.impl;

import java.lang.reflect.Field;

import com.mmxlabs.common.ITransformer;

/**
 * A delayed action which uses reflection to set a field on an object.
 * 
 * @author Simon Goodall
 * 
 */
public final class ReflectiveFieldSetter<T> implements Runnable {

	final ITransformer<T, ?> transformer;
	private final T source;
	private final Field field;
	private final Object object;

	public ReflectiveFieldSetter(final Object object, final Field field, final ITransformer<T, ?> transformer, final T source) {

		this.transformer = transformer;
		this.source = source;
		this.field = field;
		this.object = object;
	}

	@Override
	public void run() {
		final Object args = transformer.transform(source);
		try {
			field.set(object, args);
		} catch (final Exception e) {
			throw new RuntimeException("Error invoking field set on object", e);
		}
	}
}
