/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

/**
 * A cargo allocator which presumes that there are no total volume constraints,
 * and so the total remaining capacity should be allocated
 * 
 * @author Tom Hinton
 * 
 */
public class UnconstrainedCargoAllocator<T> extends BaseCargoAllocator<T> {
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.
	 * BaseCargoAllocator#allocateSpareVolume()
	 */
	@Override
	protected long[] allocateSpareVolume() {
		final long[] result = new long[cargoCount];
		for (int i = 0; i < result.length; i++) {
			result[i] = Math.min(dischargeSlots.get(i).getMaxDischargeVolume(),
					vesselCapacity.get(i) - forcedLoadVolume.get(i));
		}
		return result;
	}
}
