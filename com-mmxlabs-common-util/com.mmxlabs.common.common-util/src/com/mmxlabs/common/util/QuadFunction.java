/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.common.util;

@FunctionalInterface
public interface QuadFunction<T, U, V, W, R> {

	R apply(T t, U u, V v, W w);
}
