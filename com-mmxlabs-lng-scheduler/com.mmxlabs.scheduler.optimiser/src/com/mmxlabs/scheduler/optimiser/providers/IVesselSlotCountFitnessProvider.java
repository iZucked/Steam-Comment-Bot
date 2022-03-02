/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public interface IVesselSlotCountFitnessProvider extends IDataComponentProvider {
	int getCountForVessel(IVesselAvailability vessel);
	long getWeightForVessel(IVesselAvailability vessel);
}