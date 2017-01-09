/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario.common;

/**
 * Interface extending {@link IMultiMatrixProvider} to allow setting of values.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Key type
 * @param <U>
 *            Value type
 */
public interface IMultiMatrixEditor<T, U extends Comparable<U>> extends IMultiMatrixProvider<T, U> {

	/**
	 * Set the matrix provider for the given key.
	 * 
	 * @param x
	 * @param matrix
	 */
	void set(String key, IMatrixProvider<T, U> matrix);
}
