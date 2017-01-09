/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario.common.impl;

import java.util.HashMap;

import com.mmxlabs.optimiser.core.scenario.common.IMatrixEditor;
import com.mmxlabs.optimiser.core.scenario.common.IMatrixProvider;

/**
 * Implementation of an {@link IMatrixProvider} and {@link IMatrixEditor} using a two dimensional {@link HashMap} as the backing implementation.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Key type
 * @param <U>
 *            Value type
 */
public final class HashMapMatrixProvider<T, U> implements IMatrixProvider<T, U>, IMatrixEditor<T, U> {

	private final HashMap<T, HashMap<T, U>> matrix;

	private U defaultValue;

	public HashMapMatrixProvider() {
		this((U) null);
	}

	public HashMapMatrixProvider(final U defaultValue) {
		this.matrix = new HashMap<T, HashMap<T, U>>();
		this.defaultValue = defaultValue;
	}

	@Override
	public U get(final T x, final T y) {

		if (matrix.containsKey(x)) {
			final HashMap<T, U> row = matrix.get(x);
			if (row.containsKey(y)) {
				return row.get(y);
			}
		}

		return defaultValue;
	}

	@Override
	public void set(final T x, final T y, final U v) {
		final HashMap<T, U> row;
		if (matrix.containsKey(x)) {
			row = matrix.get(x);
		} else {
			row = new HashMap<T, U>();
			matrix.put(x, row);
		}
		row.put(y, v);
	}

	public void setDefaultValue(final U defaultValue) {
		this.defaultValue = defaultValue;
	}

	public U getDefaultValue() {
		return defaultValue;
	}

	@Override
	public boolean has(final T x, final T y) {
		return matrix.containsKey(x) && matrix.get(x).containsKey(y);
	}
}
