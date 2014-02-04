/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;

/**
 * An interface for sales contract price calculations. This calculator only have access to sequence and time information, because it will be used to calculate shipping costs.
 * 
 * @author hinton
 * 
 */
public interface ISalesPriceCalculator extends ICalculator {
	/**
	 * This method will be called once before any of the slots in the argument are evaluated using {@link #estimateSalesUnitPrice(IDischargeOption, int, IDetailTree)}, to allow for shared
	 * pre-computation.
	 * 
	 * @param sequences
	 * @since 8.0
	 */
	public void prepareEvaluation(ISequences sequences);

	/**
	 * Find the unit price in dollars per mmbtu for gas at the given slot, at the given time - volume independent. This method should always return a value as it will be used by the
	 * {@link IVoyagePlanOptimiser} and {@link ILNGVoyageCalculator} to value LNG. Final sales price will be calculated using {@link #calculateSalesUnitPrice(IDischargeOption, int, long, IDetailTree)}
	 * once volume decisions have been made.
	 * 
	 * @param time
	 * @param annotations
	 *            TODO
	 * @param slot
	 */
	public int estimateSalesUnitPrice(IDischargeOption option, int time, IDetailTree annotations);

	/**
	 * Another variant of {@link #estimateSalesUnitPrice(IDischargeOption, int, IDetailTree)} taking a discharge volume to calculate the exact price. This method should not be called before volume
	 * decisio0ns have been made.
	 * 
	 * @param time
	 * @param annotations
	 *            TODO
	 * @param slot
	 */
	public int calculateSalesUnitPrice(ILoadOption loadOption, IDischargeOption option, int loadTime, int dischargeTime, long dischargeVolumeInMMBTu, IDetailTree annotations);
}
