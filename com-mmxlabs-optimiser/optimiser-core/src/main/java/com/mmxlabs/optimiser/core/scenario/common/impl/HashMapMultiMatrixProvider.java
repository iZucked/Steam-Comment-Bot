/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario.common.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.scenario.common.IMatrixProvider;
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
public class HashMapMultiMatrixProvider<T, U extends Comparable<U>> implements
		IMultiMatrixProvider<T, U>, IMultiMatrixEditor<T, U> {

	private final HashMap<String, IMatrixProvider<T, U>> matricies;

	private final String name;

	/**
	 * Cached array of keys. This will be reset whenever a new matrix is set and
	 * recalculated when {@link #getKeys()} is called.
	 */
	private transient String[] keys;

	public HashMapMultiMatrixProvider(final String name) {
		this.name = name;
		this.matricies = new HashMap<String, IMatrixProvider<T, U>>();
	}

	@Override
	public final IMatrixProvider<T, U> get(final String key) {

		if (matricies.containsKey(key)) {
			return matricies.get(key);
		}

		return null;
	}

	@Override
	public final boolean containsKey(final String key) {

		return matricies.containsKey(key);
	}

	@Override
	public final void set(final String key, final IMatrixProvider<T, U> row) {
		// Reset keys list
		keys = null;
		matricies.put(key, row);
	}

	@Override
	public final String getName() {
		return name;
	}

	@Override
	public final void dispose() {
		matricies.clear();
		keys = null;
	}

	@Override
	public final Set<String> getKeySet() {
		return matricies.keySet();
	}

	@Override
	public final String[] getKeys() {
		if (keys == null) {
			keys = matricies.keySet().toArray(new String[matricies.size()]);
		}
		return keys;
	}

	@Override
	public final Collection<MatrixEntry<T, U>> getValues(final T x, final T y) {
		final List<MatrixEntry<T, U>> entries = new ArrayList<MatrixEntry<T, U>>(
				matricies.size());

		for (final Map.Entry<String, IMatrixProvider<T, U>> entry : matricies
				.entrySet()) {
			final String key = entry.getKey();
			final IMatrixProvider<T, U> p = entry.getValue();
			final U u = p.get(x, y);
			entries.add(new MatrixEntry<T, U>(key, x, y, u));
		}
		return entries;
	}

	/**
	 * Get the minimum distance and associated route key. This method is here
	 * for eventual precomputation optimisation
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	@Override
	public final MatrixEntry<T, U> getMinimum(final T x, final T y) {
		return Collections.min(getValues(x, y));
	}

	@Override
	public U getMinimumValue(final T x, final T y) {
		return getMinimum(x, y).getValue();
	}

	@Override
	public MatrixEntry<T, U> getMaximum(final T x, final T y) {
		/*
		 * This does not actually return the maximum value, but instead either
		 * (a) the maximum non-default value if there is a non-default value or
		 * (b) the first default value encountered if there is no non-default value.
		 */
		U invalid = null;
		U minimum = null;
		String minKey = null;

		for (final Map.Entry<String, IMatrixProvider<T, U>> entry : matricies
				.entrySet()) {
			final String key = entry.getKey();
			final IMatrixProvider<T, U> p = entry.getValue();
			if (p.has(x, y)) {
				final U u = p.get(x, y);
				if (minimum == null || u.compareTo(minimum) <= 0) {
					minimum = u;
					minKey = key;
				}
			} else if (invalid == null) {
				invalid = p.get(x, y);
				minKey = key;
			}
		}
		return new MatrixEntry<T, U>(minKey, x, y, minimum == null ? invalid
				: minimum);
	}

	@Override
	public U getMaximumValue(final T x, final T y) {
		return getMaximum(x, y).getValue();
	}
}
