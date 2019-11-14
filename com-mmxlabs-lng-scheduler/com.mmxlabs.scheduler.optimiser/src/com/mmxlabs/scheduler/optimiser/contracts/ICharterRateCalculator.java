/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

//TODO: create ICharterCostCalculator and replace ICharterRateCalculator below with new interface.
public interface ICharterRateCalculator extends ICalculator {
	/**
	 * 
	 * @param vesselAvailability
	 * @param vesselStartTime, time vessel picked up.
	 * @param voyagePlanStartTime, time loading starts.
	 * @return
	 */
	long getCharterRatePerDay(@NonNull IVesselAvailability vesselAvailability, int vesselStartTime, int voyagePlanStartTime);
}
