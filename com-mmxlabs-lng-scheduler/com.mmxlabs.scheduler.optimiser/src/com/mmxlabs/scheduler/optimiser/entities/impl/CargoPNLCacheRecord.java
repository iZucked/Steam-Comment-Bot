/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public final class CargoPNLCacheRecord {

	public @NonNull VoyagePlan plan;
	public @NonNull IVesselAvailability vesselAvailability;
	public @NonNull IAllocationAnnotation currentAllocation;
	public int vesselStartTime;
	public VolumeAllocatedSequences volumeAllocatedSequences;

	public CargoPNLCacheRecord(@NonNull final VoyagePlan plan, @NonNull final IAllocationAnnotation currentAllocation, @NonNull final IVesselAvailability vesselAvailability, final int vesselStartTime,
			@NonNull final VolumeAllocatedSequences volumeAllocatedSequences) {
		this.plan = plan;
		this.currentAllocation = currentAllocation;
		this.vesselAvailability = vesselAvailability;
		this.vesselStartTime = vesselStartTime;
		this.volumeAllocatedSequences = volumeAllocatedSequences;
	}
}
