package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermVesselSlotCountFitnessProviderEditor;

public class DefaultLongTermVesselSlotCountFitnessProvider implements ILongTermVesselSlotCountFitnessProviderEditor {

	Map<IVesselAvailability, Integer> vesselCountMap = new HashMap<>();
	Map<IVesselAvailability, Long> vesselWeightMap = new HashMap<>();
	
	@Override
	public int getCountForVessel(IVesselAvailability vessel) {
		return vesselCountMap.getOrDefault(vessel, 0);
	}

	@Override
	public long getWeightForVessel(IVesselAvailability vessel) {
		return vesselWeightMap.getOrDefault(vessel, 0L);
	}

	@Override
	public void setCountForVessel(IVesselAvailability vessel, int count) {
		vesselCountMap.put(vessel, count);
	}

	@Override
	public void setWeightForVessel(IVesselAvailability vessel, long weight) {
		vesselWeightMap.put(vessel, weight);
	}

	@Override
	public void setValuesForVessel(IVesselAvailability vessel, int count, long weight) {
		setCountForVessel(vessel, count);
		setWeightForVessel(vessel, weight);
	}

}
