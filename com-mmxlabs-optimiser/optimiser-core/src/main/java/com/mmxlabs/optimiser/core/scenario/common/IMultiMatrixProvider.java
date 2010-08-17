package com.mmxlabs.optimiser.core.scenario.common;

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
}
