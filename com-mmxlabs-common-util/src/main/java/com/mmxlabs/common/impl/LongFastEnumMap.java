/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.common.impl;

import java.util.EnumMap;

/**
 * Fast {@link EnumMap}-like implementation to link longs to an {@link Enum}.
 * This uses the enum ordinals to index an array, the size of which is specified
 * in the constructor. Unlike {@link EnumMap}, this implementation does not type
 * check the enum key, requires the number of enum elements to be specified
 * rather than rely on internal trickery and avoids boxing/unboxing by using
 * long primitives.
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

	public final long get(final K key) {
		return values[key.ordinal()];
	}

	public final void put(final K key, final long value) {
		values[key.ordinal()] = value;
	}

}
