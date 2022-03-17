/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

/**
 * {@link ICalculator} to calculate base fuel prices
 * 
 * @author achurchill
 * 
 */
@NonNullByDefault
public interface IVesselBaseFuelCalculator extends ICalculator {
	int[] getBaseFuelPrices(IVessel vessel, int voyagePlanStartTime);

	int[] getBaseFuelPrices(IVessel vessel, IPortTimesRecord portTimesRecord);
}
