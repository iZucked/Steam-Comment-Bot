package com.mmxlabs.optimiser.core.scenario.common.impl;

import java.util.HashMap;
import java.util.Set;

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
public final class HashMapMultiMatrixProvider<T, U> implements
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
}
