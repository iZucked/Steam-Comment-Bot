package com.mmxlabs.optimiser.core.scenario.common;

import java.util.Collection;
import java.util.Set;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

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
public interface IMultiMatrixProvider<T, U> extends IDataComponentProvider {

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
	public static final class MatrixEntry<T, U> {

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
}
