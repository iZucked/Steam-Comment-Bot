package com.mmxlabs.optimiser.core.scenario.common;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * Interface defining multiple two dimensional matrix with a simple value
 * getter. Each matrix has a "key" to access it.
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
	 * Return the object specified by x and y TODO: What about missing elements.
	 * 
	 * @param key
	 * @param x
	 * @param y
	 * @return
	 */
	U get(Object key, T x, T y);
}
