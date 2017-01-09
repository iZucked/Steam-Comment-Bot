/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ICustomVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Interface for volume allocation logic.
 * 
 * @author Simon Goodall, hinton
 * 
 */
public interface IVolumeAllocator {

	/**
	 * Directly create an {@link IAllocationAnnotation} from input data. Internally this should call {@link #createAllocationRecord(IVessel, int, VoyagePlan, List)}, delegate to a
	 * {@link ICustomVolumeAllocator} and then invoke {@link #allocate(AllocationRecord)}. This method is preferred over the other two for standard volume allocation.
	 * 
	 * @param vessel
	 * @param vesselStartTime
	 * @param plan
	 * @param arrivalTimes
	 * @return
	 */
	IAllocationAnnotation allocate(@NonNull IVesselAvailability vesselAvailability, int vesselStartTime, @NonNull VoyagePlan plan, @NonNull IPortTimesRecord portTimesRecord);

	/**
	 * Creates and returns the initial {@link AllocationRecord} instance based on input data. This can be modified before passing to {@link #allocate(AllocationRecord)}. This method should be called
	 * in cases where the {@link ICustomVolumeAllocator} is not expected to be invoked.
	 * 
	 * @param vessel
	 * @param vesselStartTime
	 * @param plan
	 * @param arrivalTimes
	 * @return
	 */
	AllocationRecord createAllocationRecord(@NonNull IVesselAvailability vesselAvailability, int vesselStartTime, @NonNull VoyagePlan plan, @NonNull IPortTimesRecord portTimesRecord);

	/**
	 * Given the {@link AllocationRecord}, perform the volume allocation to create an {@link IAllocationAnnotation}. This method should be called in cases where the {@link ICustomVolumeAllocator} is
	 * not expected to be invoked.
	 * 
	 * @param allocationRecord
	 * @return
	 */
	IAllocationAnnotation allocate(@NonNull AllocationRecord allocationRecord);

}