/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * An interface for shipping-side price calculations, specifically discharge contracts and cooldown price contracts. These calculators only have access to sequence and time information, because they
 * will be used to calculate shipping costs.
 * 
 * @author hinton
 * 
 */
public interface IShippingPriceCalculator<T> extends ICalculator {
	/**
	 * This method will be called once before any of the slots in the argument are evaluated using {@link #calculateUnitPrice(IPortSlot, int)}, to allow for shared pre-computation.
	 * 
	 * @param sequences
	 */
	public void prepareEvaluation(ISequences<T> sequences);

	/**
	 * Find the unit price in dollars per cubic metre for gas at the given slot, at the given time.
	 * 
	 * @param slot
	 * @param time
	 */
	public int calculateUnitPrice(IPortSlot slot, int time);
}
