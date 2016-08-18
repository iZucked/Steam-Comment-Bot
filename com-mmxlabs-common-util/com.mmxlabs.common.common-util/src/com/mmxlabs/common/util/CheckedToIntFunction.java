/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.util;

@FunctionalInterface
public interface CheckedToIntFunction<T, E extends Exception> {

	/**
	 * Applies this function to the given argument.
	 *
	 * @param t
	 *            the function argument
	 * @return the function result
	 */
	int apply(T t) throws E;
}
