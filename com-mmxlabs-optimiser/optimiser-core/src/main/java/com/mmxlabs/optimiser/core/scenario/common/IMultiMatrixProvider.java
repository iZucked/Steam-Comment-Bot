/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario.common;

import java.util.Collection;
import java.util.Set;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider.MatrixEntry;

/**
 * Interface defining multiple two dimensional matrix with a simple value
 * getter. Each matrix has a "key" to access it. A default matrix can be
 * provided using the {@link #Default_Key}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Key type
 * @param <U>
 *            Value type
 */
public interface IMultiMatrixProvider<T, U extends Comparable<U>> extends
		IDataComponentProvider {

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
	public static final class MatrixEntry<T, U extends Comparable<U>>
			implements Comparable<MatrixEntry<T, U>> {

		private final String key;

		private final T x;

		private final T y;

		private final U value;

		public MatrixEntry(final String key, final T x, final T y, final U value) {
			this.key = key;
			this.x = x;
			this.y = y;
			this.value = value;
		}

		public final String getKey() {
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

	/**
	 * Key for the default matrix.
	 */
	public static final String Default_Key = "default";

	/**
	 * Returns true if the given {@link String} exists as a key.
	 * 
	 * @param key
	 * @return
	 */
	boolean containsKey(String key);

	/**
	 * Returns the {@link IMatrixProvider} for the given key. Returns null if
	 * key is not present.
	 * 
	 * @param key
	 * @param x
	 * @param y
	 * @return
	 */
	IMatrixProvider<T, U> get(String key);

	/**
	 * Returns the keys used in this object as a {@link Set}
	 * 
	 * @return
	 */
	Set<String> getKeySet();

	/**
	 * Returns the keys used in this object as an array.
	 * 
	 * @return
	 */
	String[] getKeys();

	/**
	 * Returns the {@link Collection} of matrix entries, one from each
	 * {@link IMatrixProvider}, that corresponds to
	 * {@link IMatrixProvider#get(Object, Object)} for the given arguments.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	Collection<MatrixEntry<T, U>> getValues(T x, T y);

	/**
	 * Returns the {@link MatrixEntry} with minimum cost for points x and y
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	MatrixEntry<T, U> getMinimum(T x, T y);

	/**
	 * Returns the {@link MatrixEntry} with maximum non-default-value cost for
	 * points x and y.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	MatrixEntry<T, U> getMaximum(T x, T y);

	/**
	 * Returns the cost of the matrix entry returned by
	 * {@link IMultiMatrixProvider#getValues(T, T)}. This is the shortest
	 * distance between these two points in this multimatrix.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	U getMinimumValue(T x, T y);

	/**
	 * Returns the cost of the matrix entry returned by
	 * {@link IMultiMatrixProvider#getMaximum}. This is the longest distance
	 * between these two points which is less than the default value.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	U getMaximumValue(T x, T y);
}
