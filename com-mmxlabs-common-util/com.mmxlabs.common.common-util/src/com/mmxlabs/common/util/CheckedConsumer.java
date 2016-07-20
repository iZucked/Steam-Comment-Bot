/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.util;

@FunctionalInterface
public interface CheckedConsumer<T, E extends Exception> {

	/**
	 * Applies this function to the given argument.
	 *
	 * @param t
	 *            the function argument
	 * @return the function result
	 */
	void accept(T t) throws E;
}
