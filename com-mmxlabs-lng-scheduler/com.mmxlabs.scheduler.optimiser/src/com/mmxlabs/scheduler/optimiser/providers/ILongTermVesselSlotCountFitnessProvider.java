package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public interface ILongTermVesselSlotCountFitnessProvider {
	int getCountForVessel(IVesselAvailability vessel);

	long getWeightForVessel(IVesselAvailability vessel);
}
