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
public interface IMultiMatrixEditor<T, U> extends IMultiMatrixProvider<T, U> {

	/**
	 * Set the value v at position x, y. TODO: What if we cannot set the value? E.g. out of bounds?
	 * 
	 * @param x
	 * @param y
	 * @param v
	 */
	void set(Object key, T x, T y, U v);
}
