/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public interface ICharterRateCalculator extends ICalculator {
	long getCharterRatePerDay(@NonNull IVesselAvailability vesselAvailability, int vesselStartTime, int voyagePlanStartTime);
}
