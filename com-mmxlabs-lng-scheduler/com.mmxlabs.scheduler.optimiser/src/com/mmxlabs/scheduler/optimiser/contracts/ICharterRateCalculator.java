/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

/**
* @deprecated Use ICharterCostCalculator instead.
*/ 
public interface ICharterRateCalculator extends ICalculator {
	/**
	 * 
	 * @param vesselAvailability
	 * @param voyagePlanStartTime, time loading starts.
	 * @return
	 */
	long getCharterRatePerDay(@NonNull IVesselAvailability vesselAvailability, int voyagePlanStartTime);
}
