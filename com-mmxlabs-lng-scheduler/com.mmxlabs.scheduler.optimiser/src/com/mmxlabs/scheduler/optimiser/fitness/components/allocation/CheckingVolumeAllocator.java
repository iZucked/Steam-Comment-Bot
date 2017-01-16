/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation;

import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class CheckingVolumeAllocator implements IVolumeAllocator {
	private static final Logger log = LoggerFactory.getLogger(CheckingVolumeAllocator.class);

	private final @NonNull IVolumeAllocator reference;
	private final @NonNull IVolumeAllocator delegate;

	private long delegateSeconds = 0L;
	private long referenceSeconds = 0L;
	private long counter = 0L;

	public CheckingVolumeAllocator(final @NonNull IVolumeAllocator reference, final @NonNull CachingVolumeAllocator delegate) {
		super();
		this.reference = reference;
		this.delegate = delegate;
	}

	@Override
	public IAllocationAnnotation allocate(AllocationRecord allocationRecord) {
		return reference.allocate(allocationRecord);
	}

	@Override
	public AllocationRecord createAllocationRecord(IVesselAvailability vesselAvailability, int vesselStartTime, VoyagePlan plan, IPortTimesRecord portTimesRecord) {
		return reference.createAllocationRecord(vesselAvailability, vesselStartTime, plan, portTimesRecord);
	}

	@Override
	public IAllocationAnnotation allocate(IVesselAvailability vesselAvailability, int vesselStartTime, VoyagePlan plan, IPortTimesRecord portTimesRecord) {

		long a = System.currentTimeMillis();
		final IAllocationAnnotation value_d = delegate.allocate(vesselAvailability, vesselStartTime, plan, portTimesRecord);
		long b = System.currentTimeMillis();
		final IAllocationAnnotation value_r = reference.allocate(vesselAvailability, vesselStartTime, plan, portTimesRecord);
		long c = System.currentTimeMillis();

		delegateSeconds += (b - a);
		referenceSeconds += (c - b);

		check();

		if (!Objects.equal(value_d, value_r)) {
			Objects.equal(value_d, value_r);
			log.error("Checking VA Error: (allocation differs)");
			log.error("   reference value:" + value_r);
			log.error("    delegate value:" + value_d);
			throw new RuntimeException("Cache consistency failure");
		}

		return value_d;
	}

	private void check() {
		if (counter++ > 50_000) {
			System.out.printf("Delegate %,d - Reference %,d -- Saved %,d (%,.2f%%)\n", delegateSeconds, referenceSeconds, referenceSeconds - delegateSeconds,
					(double) (referenceSeconds - delegateSeconds) / (double) referenceSeconds * 100.0);
			counter = 0;
		}
	}
}
