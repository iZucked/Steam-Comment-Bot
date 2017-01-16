/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

/**
 * The {@link ICustomVolumeAllocator} is given an {@link AllocationRecord} after the initial values have been configured but before the volume allocation process. This permits external code to modify
 * the values - e.g. adjusting volume constraints for specific cases.
 * 
 * @author Simon Goodall
 * 
 */
public interface ICustomVolumeAllocator {

	/**
	 * Pass an {@link AllocationRecord} to the {@link ICustomVolumeAllocator} to inspect and modify if required.
	 * 
	 * @param constraint
	 */
	void modifyAllocationRecord(AllocationRecord constraint);

}
