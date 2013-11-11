/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;

/**
 * An interface for sales contract price calculations. This calculator only have access to sequence and time information, because it will be used to calculate shipping costs.
 * 
 * @author hinton
 * 
 */
public interface ISalesPriceCalculator extends ICalculator {
	/**
	 * This method will be called once before any of the slots in the argument are evaluated using {@link #calculateSalesUnitPrice(IDischargeOption, int)}, to allow for shared pre-computation.
	 * 
	 * Note the {@link ScheduledSequences} object will be in an incomplete state at this point in time.
	 * 
	 * @param sequences
	 * @param scheduledSequences
	 */
	public void prepareEvaluation(ISequences sequences, ScheduledSequences scheduledSequences);

	/**
	 * Find the unit price in dollars per mmbtu for gas at the given slot, at the given time.
	 * 
	 * @param slot
	 * @param time
	 */
	public int calculateSalesUnitPrice(IDischargeOption option, int time);
}
