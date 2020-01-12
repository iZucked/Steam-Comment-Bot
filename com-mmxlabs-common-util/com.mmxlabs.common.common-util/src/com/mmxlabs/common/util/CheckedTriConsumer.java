/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.common.util;

@FunctionalInterface
public interface CheckedTriConsumer<T, U, V, E extends Exception> {

	void accept(T t, U u, V v) throws E;
}
