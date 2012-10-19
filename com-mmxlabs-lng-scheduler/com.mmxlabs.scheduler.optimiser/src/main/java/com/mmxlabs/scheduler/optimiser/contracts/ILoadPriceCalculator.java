/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Implementations represent load-side contracts, which are invoked after scheduling and shipping level calculations.
 * 
 * @author hinton
 * 
 */
public interface ILoadPriceCalculator extends ICalculator {
	/**
	 * This will be called with a {@link ScheduledSequences} instance once before any of the load slots therein contained have their prices calculated with the
	 * {@link #calculateLoadUnitPrice(ILoadSlot, IDischargeSlot, int, int, int)} method.
	 * 
	 * @param sequences
	 */
	public void prepareEvaluation(ScheduledSequences sequences);

	/**
	 * Find the price (in $/M3) for loading at the given slot. Although every argument here except loadSlot and loadVolume can be found in the previous call to
	 * {@link #prepareEvaluation(ScheduledSequences)}, the most relevant ones are reproduced here for convenience.
	 * 
	 * 
	 * @param loadSlot
	 * @param dischargeSlot
	 * @param loadTime
	 * @param dischargeTime
	 * @param dischargePrice
	 * @return
	 */
	public int calculateLoadUnitPrice(ILoadSlot loadSlot, IDischargeSlot dischargeSlot, int loadTime, int dischargeTime, int dischargePricePerM3, int loadVolume, IVessel vessel, VoyagePlan plan,
			IDetailTree annotations);

	/**
	 * Find the price in $/m3 for loading at the given slot and discharging at the given slot, when a third-party is handling shipping
	 * 
	 * TODO decide what scheduling information is needed here
	 * 
	 * @param loadOption
	 * @param dischargeOption
	 * @return
	 */
	public int calculateLoadUnitPrice(ILoadOption loadOption, final IDischargeOption dischargeOption, final int loadTime, final int dischargePricePerM3, final int salesPricePerM3,
			IDetailTree annotations);
}
