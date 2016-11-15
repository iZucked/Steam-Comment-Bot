/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.impl;

import java.util.Arrays;
import java.util.EnumMap;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Fast {@link EnumMap}-like implementation to link longs to an {@link Enum}. This uses the enum ordinals to index an array, the size of which is specified in the constructor. Unlike {@link EnumMap},
 * this implementation does not type check the enum key, requires the number of enum elements to be specified rather than rely on internal trickery and avoids boxing/unboxing by using long primitives.
 * 
 * @author Simon Goodall
 * 
 * @param <K>
 *            Key type
 */
public final class LongFastEnumMap<K extends Enum<K>> {

	private final long[] values;

	public LongFastEnumMap(final int size) {
		values = new long[size];
	}

	public LongFastEnumMap(final int size, final long defaultValue) {
		values = new long[size];
		Arrays.fill(values, defaultValue);
	}

	public final long get(final @NonNull K key) {
		return values[key.ordinal()];
	}

	public final void put(final @NonNull K key, final long value) {
		values[key.ordinal()] = value;
	}

	/**
	 * Copy the values from the given map into this instance. Both this instance and map should have been created with the same size.
	 * 
	 * @param map
	 */
	public final void putAll(final LongFastEnumMap<K> map) {
		// We assume map.values has the same length array.
		for (int k = 0; k < values.length; ++k) {
			values[k] = map.values[k];
		}
	}

	@Override
	public final boolean equals(final Object obj) {
		if (obj instanceof LongFastEnumMap) {
			return Arrays.equals(values, ((LongFastEnumMap<?>) obj).values);
		}
		return false;
	}

	@Override
	public final int hashCode() {
		return Arrays.hashCode(values);
	}

	@Override
	public String toString() {
		return Arrays.toString(values);
	}
}
