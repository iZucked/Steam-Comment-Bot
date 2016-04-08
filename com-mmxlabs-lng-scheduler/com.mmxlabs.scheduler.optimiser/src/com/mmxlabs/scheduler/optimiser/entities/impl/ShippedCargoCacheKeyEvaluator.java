/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.caches.AbstractCache.IKeyEvaluator;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.CargoValueAnnotation;

public class ShippedCargoCacheKeyEvaluator implements IKeyEvaluator<@NonNull CargoPNLCacheRecord, @NonNull Pair<@NonNull CargoValueAnnotation, @NonNull Long>> {
	private final @NonNull IEntityValueCalculator delegate;

	public ShippedCargoCacheKeyEvaluator(final @NonNull IEntityValueCalculator delegate) {
		this.delegate = delegate;
	}

	@Override
	final public @NonNull Pair<@NonNull CargoPNLCacheRecord, @NonNull Pair<@NonNull CargoValueAnnotation, @NonNull Long>> evaluate(final @NonNull CargoPNLCacheRecord key) {

		final Pair<@NonNull CargoValueAnnotation, @NonNull Long> result = delegate.evaluate(key.plan, key.currentAllocation, key.vesselAvailability, key.vesselStartTime, null);
		return new Pair<>(key, result);
	}

}
