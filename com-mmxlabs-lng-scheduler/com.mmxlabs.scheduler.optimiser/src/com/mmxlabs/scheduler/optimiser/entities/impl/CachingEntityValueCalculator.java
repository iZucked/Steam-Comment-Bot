/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities.impl;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.caches.AbstractCache;
import com.mmxlabs.common.caches.LHMCache;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.cache.CacheKey;
import com.mmxlabs.scheduler.optimiser.cache.DepCacheKey;
import com.mmxlabs.scheduler.optimiser.cache.IProfitAndLossCacheKeyDependencyLinker;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.CargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public final class CachingEntityValueCalculator implements IEntityValueCalculator {

	@Inject
	private IProfitAndLossCacheKeyDependencyLinker linker;

	private final @NonNull IEntityValueCalculator delegate;

	private final @NonNull AbstractCache<@NonNull CacheKey<@NonNull CargoPNLCacheRecord>, @Nullable Pair<@NonNull CargoValueAnnotation, @NonNull Long>> cache;

	public CachingEntityValueCalculator(final @NonNull IEntityValueCalculator delegate) {
		this(delegate, 500_000);
	}

	public CachingEntityValueCalculator(final @NonNull IEntityValueCalculator delegate, final int cacheSize) {
		super();
		this.delegate = delegate;

		this.cache = new LHMCache<>("CargoPNLCache", key -> {
			try {
				return new Pair<>(key, delegate.evaluate(key.getRecord().plan, key.getRecord().currentAllocation, key.getRecord().vesselAvailability, key.getRecord().vesselStartTime,
						key.getRecord().volumeAllocatedSequences, null));
			} finally {
				// Null out after calculation
				key.getRecord().volumeAllocatedSequences = null;
			}
		}, cacheSize);
	}

	@Override
	public Pair<@NonNull CargoValueAnnotation, @NonNull Long> evaluate(@NonNull final VoyagePlan plan, @NonNull final IAllocationAnnotation currentAllocation,
			@NonNull final IVesselAvailability vesselAvailability, final int vesselStartTime, @Nullable final VolumeAllocatedSequences volumeAllocatedSequences,
			@Nullable final IAnnotatedSolution annotatedSolution) {
		if (annotatedSolution != null || volumeAllocatedSequences == null) {
			return delegate.evaluate(plan, currentAllocation, vesselAvailability, vesselStartTime, volumeAllocatedSequences, annotatedSolution);
		}
		final CargoPNLCacheRecord record = new CargoPNLCacheRecord(plan, currentAllocation, vesselAvailability, vesselStartTime, volumeAllocatedSequences);
		final List<@NonNull DepCacheKey> depKeys = linker.link(currentAllocation, volumeAllocatedSequences);
		final CacheKey<@NonNull CargoPNLCacheRecord> key = new CacheKey<>(vesselAvailability, currentAllocation.getStartHeelVolumeInM3(), currentAllocation, record, depKeys);

		return cache.get(key);
	}

	@Override
	public long evaluate(@NonNull final VoyagePlan plan, @NonNull final IVesselAvailability vesselAvailability, final int planStartTime, final int vesselStartTime,
			@Nullable final VolumeAllocatedSequences volumeAllocatedSequences, @Nullable final IAnnotatedSolution annotatedSolution) {
		return delegate.evaluate(plan, vesselAvailability, planStartTime, vesselStartTime, volumeAllocatedSequences, annotatedSolution);
	}

	@Override
	public long evaluateUnusedSlot(@NonNull final IPortSlot portSlot, @Nullable final VolumeAllocatedSequences volumeAllocatedSequences, @Nullable final IAnnotatedSolution annotatedSolution) {
		return delegate.evaluateUnusedSlot(portSlot, volumeAllocatedSequences, annotatedSolution);
	}
}
