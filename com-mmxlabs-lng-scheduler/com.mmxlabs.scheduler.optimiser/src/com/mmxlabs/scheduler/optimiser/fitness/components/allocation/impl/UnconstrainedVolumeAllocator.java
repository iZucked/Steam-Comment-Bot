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
 * @since 6.0
 * 
 */
public class UnconstrainedVolumeAllocator extends BaseVolumeAllocator {

	/**
	 * Returns x, capped by y; if x has the special value 0, it is considered undefined and y is returned. 
	 * @return
	 */
	protected final static long capValueWithZeroDefault(long x, long y) {
		return x == 0 ? y : Math.min(x, y);
	}
	
	
	/**
	 * Calculates the load / discharge volumes per slot in a cargo, based on the constraints
	 * supplied (the heel which has to remain at the end of the cargo, the amount of LNG required for travel, 
	 * and the vessel capacity). There may also be constraints placed on the amount which can be discharged
	 * or loaded per slot.
	 * 
	 * Assumes that the maximum amount within available constraints will be loaded or discharged at each
	 * slot.  
	 * 
	 * @param constraint
	 * @return
	 */
	protected final static void allocateBasicSlotVolumes(AllocationRecord constraint) {
		final IPortSlot[] slots = constraint.slots;		
		final long [] result = constraint.allocations;
		
		ILoadOption loadSlot = (ILoadOption) slots[0];
		long availableCargoSpace = constraint.vesselCapacityInM3 - constraint.startVolumeInM3;
		
		// greedy assumption: always load as much as possible
		long loadVolume = capValueWithZeroDefault(loadSlot.getMaxLoadVolume(), availableCargoSpace);
		
		// available volume is non-negative
		long unusedVolume = Math.max(loadVolume + constraint.startVolumeInM3 - constraint.minEndVolumeInM3 - constraint.requiredFuelVolumeInM3, 0);

		// load / discharge case
		// this is subsumed by the LDD* case, but can be done more efficiently 
		if (slots.length == 2) {
			
			IDischargeOption dischargeSlot = (IDischargeOption) slots[1]; 

			// greedy assumption: always discharge as much as possible
			long dischargeVolume = capValueWithZeroDefault(dischargeSlot.getMaxDischargeVolume(), unusedVolume);

			// TODO: this method does not yet enforce minimum load / discharge constraints
			
			result[1] = dischargeVolume;
			unusedVolume -= dischargeVolume;

		}		
		// multiple load/discharge case
		else {
			// TODO: this only handles LDD* cases
			
			// track which discharge slot is the most profitable 
			int [] prices = constraint.slotPricesPerM3;
			int mostProfitableDischargeIndex = 1;
			
			// assign the minimum amount per discharge slot
			for (int i = 1; i < slots.length; i++) {
				IDischargeOption dischargeSlot = (IDischargeOption) slots[i];
				long minDischargeVolume = dischargeSlot.getMinDischargeVolume();
				
				// assign the minimum amount per discharge slot
				result[i] = minDischargeVolume;
				unusedVolume -= minDischargeVolume;
				
				// more profitable ? 
				if (i > 1 && prices[i] > prices[mostProfitableDischargeIndex]) {
					mostProfitableDischargeIndex = i;
				}
			}
			
			int nDischargeSlots = slots.length - 1;
			
			// now, starting with the most profitable discharge slot, allocate
			// any remaining volume
			for (int i = 0; i < nDischargeSlots && unusedVolume > 0; i++) {
				// start at the most profitable slot and cycle through them in order
				// TODO: would be better to sort them by profitability, but needs to be done efficiently
				int index = 1 + ((i + mostProfitableDischargeIndex) % nDischargeSlots);
				
				IDischargeOption slot = (IDischargeOption) slots[index];
				// discharge all remaining volume at this slot, up to the slot maximum 
				long volume = Math.min(slot.getMaxDischargeVolume(), unusedVolume);
				// reduce the remaining available volume 
				unusedVolume -= volume - result[index];
				result[index] = volume;
			}
			
			// Note this currently does nothing as the next() method in the allocator iterator (BaseCargoAllocator) ignores this data and looks directly on the discharge slot.
		}
		
		/* Under certain circumstances, the remaining heel may be more than expected:
		 * for instance, when a minimum load constraint far exceeds a maximum discharge constraint.
		 * This may cause problems with restrictions on where LNG can be shipped, or with profit share contracts,
		 * so at present a conservative assumption in the LNGVoyageCalculator treats it as a capacity violation
		 * and assumes that the load has to be reduced below its min value constraint.
		 */

		// if there is any leftover volume after discharge
		if (unusedVolume > 0) {
			// we use a conservative heuristic: load exactly as much as we need, subject to constraints
			long revisedLoadVolume = Math.max(loadVolume - unusedVolume, loadSlot.getMinLoadVolume());
			
			unusedVolume -= loadVolume - revisedLoadVolume;
			loadVolume = revisedLoadVolume;
			/* TODO: if the max discharge volume would leave excess heel, the correct load volume 
			 * decision depends on relative prices at this load and the next, and whether carrying over excess heel is contractually
			 * permissible (and CV-compatible with the next load port).
			 */ 			
		}		

		constraint.allocatedEndVolumeInM3 = constraint.minEndVolumeInM3;
		// constraint.allocatedEndVolumeInM3 = constraint.minEndVolumeInM3 + unusedVolume;
		
		result[0] = loadVolume;

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl. BaseCargoAllocator#allocateSpareVolume()
	 */
	@Override
	protected void allocateSpareVolume() {
		for (AllocationRecord constraint: constraints) {
			allocateBasicSlotVolumes(constraint);
		}
	}
}
