/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario.common;

/**
 * Interface extending {@link IMatrixProvider} to allow setting of values.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Key type
 * @param <U>
 *            Value type
 */
public interface IMatrixEditor<T, U> extends IMatrixProvider<T, U> {

	/**
	 * Set the value v at position x, y. TODO: What if we cannot set the value? E.g. out of bounds?
	 * 
	 * @param x
	 * @param y
	 * @param v
	 */
	void set(T x, T y, U v);
}
