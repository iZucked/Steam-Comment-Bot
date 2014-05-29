/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

import com.mmxlabs.scheduler.optimiser.components.IVessel;


public interface ICharterRateCalculator extends ICalculator {
	int getCharterRatePerDay(IVessel vessel, int vesselStartTime, int voyagePlanStartTime);
}
