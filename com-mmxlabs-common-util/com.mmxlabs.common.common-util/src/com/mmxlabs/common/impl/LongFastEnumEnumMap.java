/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.impl;

import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNull;

/**
 * A two-dimensional EnumxEnum to long map implementation. See {@link LongFastEnumMap} for more details.
 * 
 * @author Simon Goodall
 * 
 * @See {@link LongFastEnumMap}
 * 
 * @param <K>
 *            Key type 1
 * @param <L>
 *            Key type 2
 */
public final class LongFastEnumEnumMap<K extends Enum<K>, L extends Enum<L>> {

	private final long[][] values;

	public LongFastEnumEnumMap(final int size1, final int size2) {
		values = new long[size1][];
		for (int i = 0; i < size1; ++i) {
			values[i] = new long[size2];
		}
	}

	public final long get(final @NonNull K key1, final @NonNull L key2) {
		return values[key1.ordinal()][key2.ordinal()];
	}

	public final void put(final @NonNull K key1, final @NonNull L key2, final long value) {
		values[key1.ordinal()][key2.ordinal()] = value;
	}

	@Override
	public final boolean equals(final Object obj) {
		if (obj instanceof LongFastEnumEnumMap) {

			final long[][] values2 = ((LongFastEnumEnumMap<?, ?>) obj).values;
			if (values.length == values2.length) {
				for (int i = 0; i < values.length; ++i) {
					if (!Arrays.equals(values[i], values2[i])) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public final int hashCode() {

		return Arrays.deepHashCode(values);
	}
}
