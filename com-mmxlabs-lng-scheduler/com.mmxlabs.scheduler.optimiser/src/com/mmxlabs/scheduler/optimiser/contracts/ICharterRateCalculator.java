/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;


public interface ICharterRateCalculator extends ICalculator {
	int getCharterRatePerDay(IVesselAvailability vesselAvailability, int vesselStartTime, int voyagePlanStartTime);
}
