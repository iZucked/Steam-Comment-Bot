/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

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
			// Total volume required for basic travel
			final long flv = forcedLoadVolumeInM3.get(i) + remainingHeelVolumeInM3.get(i);
			
			IPortSlot[] slots = listedSlots.get(i);
			assert(slots.length == 2);
			long maxLoadVolume = ((ILoadOption) (slots[0])).getMaxLoadVolume();
			if (maxLoadVolume == 0) {
				maxLoadVolume = vesselCapacityInM3.get(i);
			}
			long maxDischargeVolume = ((IDischargeOption) (slots[1])).getMaxDischargeVolume();
			if (maxDischargeVolume == 0) {
				maxDischargeVolume =vesselCapacityInM3.get(i) - flv;
			}else {
				maxDischargeVolume = Math.min(maxDischargeVolume, vesselCapacityInM3.get(i) - flv);
			}
			result[i] = Math.min(maxLoadVolume - flv, maxDischargeVolume);
		}
		return result;
	}
}
