/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common;

import java.util.HashMap;

/**
 * An extension of HashMap<Pair<K1, K2>, V> with convenience methods for getting and setting.
 * 
 * @author hinton
 * 
 */
public class PairKeyedMap<K1, K2, V> extends HashMap<Pair<K1, K2>, V> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public V get(final K1 k1, final K2 k2) {
		return get(new Pair<>(k1, k2));
	}

	public V put(final K1 k1, final K2 k2, final V value) {
		return put(new Pair<>(k1, k2), value);
	}

	public boolean contains(final K1 k1, final K2 k2) {
		return containsKey(new Pair<>(k1, k2));
	}
}
