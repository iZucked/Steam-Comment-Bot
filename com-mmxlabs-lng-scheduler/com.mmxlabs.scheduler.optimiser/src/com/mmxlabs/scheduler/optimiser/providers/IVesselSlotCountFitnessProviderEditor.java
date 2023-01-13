/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;

public interface IVesselSlotCountFitnessProviderEditor extends IVesselSlotCountFitnessProvider {
	
	void setCountForVessel(IVesselCharter vessel, int count);
	void setWeightForVessel(IVesselCharter vessel, long weight);
	void setValuesForVessel(IVesselCharter vessel, int count, long weight);

}
