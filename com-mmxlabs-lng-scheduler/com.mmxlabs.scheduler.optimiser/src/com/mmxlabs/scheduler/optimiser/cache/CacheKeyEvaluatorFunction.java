/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.cache;

import org.eclipse.jdt.annotation.NonNull;

@FunctionalInterface
public interface CacheKeyEvaluatorFunction<T, U> {
	U evaluate(@NonNull T record);
}