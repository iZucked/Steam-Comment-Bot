/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

/**
 * A cargo allocator which presumes that there are no total volume constraints, and so the total remaining capacity should be allocated
 * 
 * @author Tom Hinton
 * 
 */
public class UnconstrainedCargoAllocator extends BaseCargoAllocator {
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl. BaseCargoAllocator#allocateSpareVolume()
	 */
	@Override
	protected long[] allocateSpareVolume() {
		final long[] result = new long[cargoCount];
		for (int i = 0; i < result.length; i++) {
			final long flv = forcedLoadVolume.get(i);
			result[i] = Math.min(loadSlots.get(i).getMaxLoadVolume() - flv, Math.min(dischargeSlots.get(i).getMaxDischargeVolume(), vesselCapacity.get(i) - flv));
		}
		return result;
	}
}
