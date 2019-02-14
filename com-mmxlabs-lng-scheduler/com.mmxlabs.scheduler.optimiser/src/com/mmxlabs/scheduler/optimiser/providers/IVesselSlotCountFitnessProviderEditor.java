/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public interface IVesselSlotCountFitnessProviderEditor extends IVesselSlotCountFitnessProvider {
	
	void setCountForVessel(IVesselAvailability vessel, int count);
	void setWeightForVessel(IVesselAvailability vessel, long weight);
	void setValuesForVessel(IVesselAvailability vessel, int count, long weight);

}
