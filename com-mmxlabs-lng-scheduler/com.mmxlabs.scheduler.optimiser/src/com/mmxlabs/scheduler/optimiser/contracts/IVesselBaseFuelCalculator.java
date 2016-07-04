/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

/**
 * {@link ICalculator} to calculate base fuel prices
 * 
 * @author achurchill
 * 
 */
public interface IVesselBaseFuelCalculator extends ICalculator {
	int getBaseFuelPrice(IVessel vessel, int voyagePlanStartTime);

	int getBaseFuelPrice(IVessel vessel, IPortTimesRecord portTimesRecord);

	int getBaseFuelPrice(IVesselClass vesselClass, int voyagePlanStartTime);
}
