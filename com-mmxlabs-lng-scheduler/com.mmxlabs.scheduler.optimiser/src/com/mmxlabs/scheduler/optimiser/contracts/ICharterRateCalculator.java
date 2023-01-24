/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;

/**
* @deprecated Use ICharterCostCalculator instead.
*/ 
public interface ICharterRateCalculator extends ICalculator {
	/**
	 * 
	 * @param vesselCharter
	 * @param voyagePlanStartTime, time loading starts.
	 * @return
	 */
	long getCharterRatePerDay(@NonNull IVesselCharter vesselCharter, int voyagePlanStartTime);
}
