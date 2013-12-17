package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.BaseVolumeAllocator.AllocationRecord;

/**
 * Implementation of the {@link ICustomVolumeAllocator} permit the default behaviour of an {@link IVolumeAllocator} to change on a per cargo basis.
 * 
 * @author Simon Goodall
 * 
 */
public interface ICustomVolumeAllocator {

	/**
	 * Returns true if this {@link ICustomVolumeAllocator} wishes to take over the volume allocation
	 * 
	 * @param constraint
	 * @return
	 */
	boolean canHandle(AllocationRecord constraint);

	/**
	 * Perform the volume allocation instead of the main {@link IVolumeAllocator}. This assume a prior call to {@link #canHandle(AllocationRecord)} has returned true.
	 * 
	 * @param constraint
	 */
	void handle(AllocationRecord constraint);

}
