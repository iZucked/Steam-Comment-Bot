/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;

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
	 * @param sequences
	 */
	public void prepareEvaluation(ISequences sequences);

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
