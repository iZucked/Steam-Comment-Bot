package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public interface ILongTermVesselSlotCountFitnessProviderEditor extends ILongTermVesselSlotCountFitnessProvider {
	
	void setCountForVessel(IVesselAvailability vessel, int count);
	void setWeightForVessel(IVesselAvailability vessel, long weight);
	void setValuesForVessel(IVesselAvailability vessel, int count, long weight);

}
