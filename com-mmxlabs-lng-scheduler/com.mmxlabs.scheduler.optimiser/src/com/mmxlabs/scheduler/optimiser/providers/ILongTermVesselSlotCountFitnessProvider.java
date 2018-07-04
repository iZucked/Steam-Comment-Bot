package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public interface ILongTermVesselSlotCountFitnessProvider extends IDataComponentProvider {
	int getCountForVessel(IVesselAvailability vessel);
	long getWeightForVessel(IVesselAvailability vessel);
}