/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common;

import java.util.HashMap;

/**
 * Class to store cumulative numeric data by key.
 */
public class CumulativeMap<T> extends HashMap<T, Double> {
	private static final long serialVersionUID = 1L;

	/**
	 * Adds a cumulative numeric value to the map. If the key is contained in the map, the new value is added to the existing value. Otherwise, the new value is entered into the map. In other words,
	 * the map conceptually defaults new keys to zero before adding the specified value.
	 * 
	 * @param key
	 * @param value
	 */
	public void plusEquals(final T key, final Double value) {
		if (containsKey(key)) {
			put(key, get(key) + value);
		} else {
			put(key, value);
		}
	}
}