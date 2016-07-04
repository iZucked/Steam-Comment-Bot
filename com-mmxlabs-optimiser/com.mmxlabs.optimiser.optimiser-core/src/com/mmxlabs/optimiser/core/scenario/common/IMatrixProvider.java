/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
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

	/**
	 * Return true if the edge from x to y has been set.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	boolean has(T x, T y);
}
