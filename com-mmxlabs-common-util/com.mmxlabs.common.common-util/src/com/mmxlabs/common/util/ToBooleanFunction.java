/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.util;

@FunctionalInterface
public interface ToBooleanFunction<T> {
	boolean accept(T t);
}
