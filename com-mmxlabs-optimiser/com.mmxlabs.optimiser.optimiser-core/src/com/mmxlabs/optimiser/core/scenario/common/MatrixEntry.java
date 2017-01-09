/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario.common;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Class representing an entry from a {@link IMatrixProvider}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Key type
 * 
 * @param <U>
 *            Value type
 */
public final class MatrixEntry<T, U extends Comparable<U>> implements Comparable<MatrixEntry<T, U>> {

	private final @NonNull String key;

	private final T x;

	private final T y;

	private final U value;

	public MatrixEntry(@NonNull final String key, final T x, final T y, final U value) {
		this.key = key;
		this.x = x;
		this.y = y;
		this.value = value;
	}

	public final @NonNull String getKey() {
		return key;
	}

	public final T getX() {
		return x;
	}

	public final T getY() {
		return y;
	}

	public final U getValue() {
		return value;
	}

	@Override
	public final int compareTo(final MatrixEntry<T, U> other) {
		return value.compareTo(other.value);
	}
}