/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.caches.AbstractCache;
import com.mmxlabs.common.caches.LHMCache;
import com.mmxlabs.scheduler.optimiser.cache.CacheKey;
import com.mmxlabs.scheduler.optimiser.cache.FunctionalCacheKeyEvaluator;
import com.mmxlabs.scheduler.optimiser.cache.ICacheKeyDependencyLinker;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public final class CachingVolumeAllocator implements IVolumeAllocator {
	@Inject
	private ICacheKeyDependencyLinker linker;

	private final IVolumeAllocator delegate;

	private final AbstractCache<CacheKey<@NonNull CargoVolumeCacheRecord>, @Nullable IAllocationAnnotation> cache;

	public CachingVolumeAllocator(final @NonNull IVolumeAllocator delegate) {
		this(delegate, 500_000);
	}

	public CachingVolumeAllocator(final @NonNull IVolumeAllocator delegate, final int cacheSize) {
		super();
		this.delegate = delegate;

		this.cache = new LHMCache<>("VolumeAllocatorCache", new FunctionalCacheKeyEvaluator<>(record -> {
			return delegate.allocate(record.vesselAvailability, record.vesselStartTime, record.plan, record.portTimesRecord);
		}), cacheSize);
	}

	@Override
	public IAllocationAnnotation allocate(final @NonNull IVesselAvailability vesselAvailability, final int vesselStartTime, final @NonNull VoyagePlan plan,
			final @NonNull IPortTimesRecord portTimesRecord) {
		final CargoVolumeCacheRecord record = new CargoVolumeCacheRecord(vesselAvailability, vesselStartTime, plan, portTimesRecord);

		// final List<CacheKey<@NonNull CargoVolumeCacheRecord>> depKeys = linker.link(CacheType.Volume, portTimesRecord);
		// final CacheKey<@NonNull CargoVolumeCacheRecord> key = new CacheKey<>(vesselAvailability, plan.getStartingHeelInM3(), portTimesRecord, record, depKeys);

		final CacheKey<@NonNull CargoVolumeCacheRecord> key = new CacheKey<>(vesselAvailability, plan.getStartingHeelInM3(), portTimesRecord, record);

		return cache.get(key);
	}

	@Override
	public IAllocationAnnotation allocate(final @NonNull AllocationRecord allocationRecord) {
		return delegate.allocate(allocationRecord);
	}

	@Override
	public AllocationRecord createAllocationRecord(final @NonNull IVesselAvailability vesselAvailability, final int vesselStartTime, final @NonNull VoyagePlan plan,
			final @NonNull IPortTimesRecord portTimesRecord) {
		return delegate.createAllocationRecord(vesselAvailability, vesselStartTime, plan, portTimesRecord);
	}
}
