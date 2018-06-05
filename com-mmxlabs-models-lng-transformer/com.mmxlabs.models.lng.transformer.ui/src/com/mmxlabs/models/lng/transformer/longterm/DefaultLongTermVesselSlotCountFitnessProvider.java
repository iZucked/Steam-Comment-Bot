package com.mmxlabs.models.lng.transformer.longterm;

import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public class DefaultLongTermVesselSlotCountFitnessProvider implements ILongTermVesselSlotCountFitnessProvider {

	@Override
	public int getCountForVessel(IVesselAvailability vessel) {
		return 0;
	}

	@Override
	public long getWeightForVessel(IVesselAvailability vessel) {
		return 0;
	}

}
