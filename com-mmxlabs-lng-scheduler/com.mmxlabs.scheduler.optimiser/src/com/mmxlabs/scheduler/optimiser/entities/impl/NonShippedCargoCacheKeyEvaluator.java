package com.mmxlabs.scheduler.optimiser.entities.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.caches.AbstractCache.IKeyEvaluator;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;

public class NonShippedCargoCacheKeyEvaluator implements IKeyEvaluator<@NonNull NonShippedCargoCacheKey, @NonNull Long> {

	private final @NonNull IEntityValueCalculator delegate;

	public NonShippedCargoCacheKeyEvaluator(final @NonNull IEntityValueCalculator delegate) {
		this.delegate = delegate;
	}

	@Override
	final public @NonNull Pair<@NonNull NonShippedCargoCacheKey, @NonNull Long> evaluate(final NonShippedCargoCacheKey key) {

		final long result = delegate.evaluate(key.plan, key.vesselAvailability, key.planStartTime, key.vesselStartTime, null);
		return new Pair<>(key, result);
	}
}
