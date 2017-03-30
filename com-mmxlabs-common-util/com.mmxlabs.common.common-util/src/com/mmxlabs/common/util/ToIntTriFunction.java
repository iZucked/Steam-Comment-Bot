/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.util;

@FunctionalInterface
public interface ToIntTriFunction<T, U, V> {

	int applyAsInt(T t, U u, V v);
}