package com.mmxlabs.optimiser.scenario.common.impl;

import java.util.HashMap;

import com.mmxlabs.optimiser.scenario.common.IMatrixEditor;
import com.mmxlabs.optimiser.scenario.common.IMatrixProvider;

/**
 * Implementation of an {@link IMatrixProvider} and {@link IMatrixEditor} using
 * a two dimensional {@link HashMap} as the backing implementation.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Key type
 * @param <U>
 *            Value type
 */
public final class HashMapMatrixProvider<T, U> implements
		IMatrixProvider<T, U>, IMatrixEditor<T, U> {

	private HashMap<T, HashMap<T, U>> matrix;

	private final String name;

	public HashMapMatrixProvider(final String name) {
		this.name = name;
		this.matrix = new HashMap<T, HashMap<T,U>>();
	}

	@Override
	public U get(final T x, final T y) {

		if (matrix.containsKey(x)) {
			final HashMap<T, U> row = matrix.get(x);
			if (row.containsKey(y)) {
				return row.get(y);
			}
		}

		// TODO: null or exception?
		return null;
	}

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

	@Override
	public String getName() {
		return name;
	}
}
