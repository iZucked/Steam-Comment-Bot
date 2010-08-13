package com.mmxlabs.optimiser.core.scenario.common.impl;

import java.util.HashMap;

import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixEditor;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;

/**
 * Implementation of an {@link IMultiMatrixProvider} and
 * {@link IMultiMatrixEditor} using {@link HashMapMatrixProvider} as the backing
 * implementation.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Key type
 * @param <U>
 *            Value type
 */
public final class HashMapMultiMatrixProvider<T, U> implements
		IMultiMatrixProvider<T, U>, IMultiMatrixEditor<T, U> {

	private final HashMap<Object, HashMapMatrixProvider<T, U>> matrix;

	private final String name;

	private U defaultValue;

	public HashMapMultiMatrixProvider(final String name) {
		this(name, null);
	}

	public HashMapMultiMatrixProvider(final String name, final U defaultValue) {
		this.name = name;
		this.matrix = new HashMap<Object, HashMapMatrixProvider<T, U>>();
		this.defaultValue = defaultValue;
	}

	@Override
	public U get(Object key, final T x, final T y) {

		if (matrix.containsKey(key)) {
			final HashMapMatrixProvider<T, U> row = matrix.get(key);
			return row.get(x, y);
		}

		return defaultValue;
	}

	@Override
	public void set(Object key, final T x, final T y, final U v) {
		final HashMapMatrixProvider<T, U> row;
		if (matrix.containsKey(key)) {
			row = matrix.get(key);
		} else {
			row = new HashMapMatrixProvider<T, U>(key.toString(), defaultValue);
			matrix.put(key, row);
		}
		row.set(x, y, v);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		matrix.clear();
	}

	public void setDefaultValue(U defaultValue) {
		this.defaultValue = defaultValue;
	}

	public U getDefaultValue() {
		return defaultValue;
	}
}
