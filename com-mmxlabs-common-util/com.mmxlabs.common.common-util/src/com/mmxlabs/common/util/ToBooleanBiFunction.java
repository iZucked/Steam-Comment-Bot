/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.common.util;

@FunctionalInterface
public interface ToBooleanBiFunction<T, U> {
	boolean accept(T t, U u);
}
