package com.mmxlabs.models.lng.transformer.optimiser.longterm;

import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public interface ILongTermVesselSlotCountFitnessProvider {
	int getCountForVessel(IVesselAvailability vessel);
	long getWeightForVessel(IVesselAvailability vessel);
}
