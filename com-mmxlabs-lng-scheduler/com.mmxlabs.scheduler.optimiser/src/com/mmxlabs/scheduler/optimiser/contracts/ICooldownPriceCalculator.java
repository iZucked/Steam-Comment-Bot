/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;

/**
 * An interface for cooldown price calculations. This calculator only has access to sequence and time information, because it will be used to calculate shipping costs.
 * 
 * @author hinton
 * 
 */
public interface ICooldownPriceCalculator extends ICalculator {
	/**
	 * This method will be called once before any of the slots in the argument are evaluated using {@link #calculateCooldownUnitPrice(ILoadSlot, int)}, to allow for shared pre-computation.
	 * 
	 * Note the {@link ScheduledSequences} object will be in an incomplete state at this point in time.
	 * 
	 * @param sequences
	 * @param scheduledSequences
	 */
	public void prepareEvaluation(ISequences sequences, ScheduledSequences scheduledSequences);

	/**
	 * Find the unit price in dollars per mmbtu for gas at the given {@link ILoadSlot}, at the given time.
	 * 
	 * @param slot
	 * @param time
	 */
	public int calculateCooldownUnitPrice(ILoadSlot option, int time);

	/**
	 * Find the unit price in dollars per mmbtu for gas at the given time. To be used when an {@link ILoadSlot} is not available.
	 * 
	 * @param time
	 * @since 2.0
	 */
	public int calculateCooldownUnitPrice(int time);
}
