/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.delayedactions.impl;

import java.lang.reflect.Method;

import com.mmxlabs.common.ITransformer;

public final class ReflectiveMethodSetter<T> implements Runnable {

	final ITransformer<T, ?> transformer;
	private final T source;
	private final Method method;
	private final Object object;

	public ReflectiveMethodSetter(final Object object, final Method method, final ITransformer<T, ?> transformer, final T source) {

		this.transformer = transformer;
		this.source = source;
		this.method = method;
		this.object = object;
	}

	@Override
	public void run() {
		final Object args = transformer.transform(source);
		try {
			method.invoke(object, args);
		} catch (final Exception e) {
			throw new RuntimeException("Error invoking method on object", e);
		}
	}
}
