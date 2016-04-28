/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

/**
 * An interface for cooldown price calculations. This calculator only has access to sequence and time information, because it will be used to calculate shipping costs.
 * 
 * @author hinton
 * 
 */
public interface ICooldownCalculator extends ICalculator {
	/**
	 * This method will be called once before any of the slots in the argument are evaluated using {@link #calculateCooldownUnitPrice(ILoadSlot, int)}, to allow for shared pre-computation.
	 * 
	 * Note the {@link ScheduledSequences} object will be in an incomplete state at this point in time.
	 * 
	 * @param sequences
	 * @param scheduledSequences
	 */
	public void prepareEvaluation(@NonNull ISequences sequences);

	/**
	 * Calculate the total cost of a cooldown operation. Note: time is passed in local time and converted to UTC.
	 * 
	 * @param time
	 * @param port
	 *            {@link IPort} for local to UTC conversion
	 */
	public long calculateCooldownCost(@NonNull IVesselClass vesselClass, @NonNull IPort port, int cv, int localTime);
}
