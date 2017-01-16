/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.cache;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.caches.AbstractCache.IKeyEvaluator;

public final class FunctionalCacheKeyEvaluator<T, U> implements IKeyEvaluator<CacheKey<T>, U> {

	private final @NonNull CacheKeyEvaluatorFunction<T, U> function;

	public FunctionalCacheKeyEvaluator(final @NonNull CacheKeyEvaluatorFunction<T, U> function) {
		this.function = function;
	}

	@Override
	public Pair<CacheKey<T>, U> evaluate(final CacheKey<T> key) {
		final U result = function.evaluate(key.getRecord());
		return new Pair<>(key, result);
	}

}