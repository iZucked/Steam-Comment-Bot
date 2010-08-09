package com.mmxlabs.optimiser.core.scenario.common;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * Interface defining a two dimensional matrix with a simple value getter.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Key type
 * @param <U>
 *            Value type
 */
public interface IMatrixProvider<T, U> extends IDataComponentProvider {

	/**
	 * Return the object specified by x and y TODO: What about missing elements.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	U get(T x, T y);
}
