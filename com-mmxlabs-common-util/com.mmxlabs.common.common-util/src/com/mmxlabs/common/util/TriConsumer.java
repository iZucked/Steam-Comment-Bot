/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */

package com.mmxlabs.common.util;

@FunctionalInterface
public interface TriConsumer<T, U, V> {

	void accept(T t, U u, V v);
}
