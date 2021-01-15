/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.common.util;

@FunctionalInterface
public interface CheckedSupplier<T, E extends Exception> {

	/**
	 *
	 * @return the function result
	 */
	T get() throws E;
}
