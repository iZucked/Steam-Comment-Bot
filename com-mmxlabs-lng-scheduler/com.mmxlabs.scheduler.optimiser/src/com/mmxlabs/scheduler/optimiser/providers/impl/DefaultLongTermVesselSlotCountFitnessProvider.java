/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.providers.IVesselSlotCountFitnessProviderEditor;

public class DefaultLongTermVesselSlotCountFitnessProvider implements IVesselSlotCountFitnessProviderEditor {

	Map<IVesselCharter, Integer> vesselCountMap = new HashMap<>();
	Map<IVesselCharter, Long> vesselWeightMap = new HashMap<>();
	
	@Override
	public int getCountForVessel(IVesselCharter vessel) {
		return vesselCountMap.getOrDefault(vessel, 0);
	}

	@Override
	public long getWeightForVessel(IVesselCharter vessel) {
		return vesselWeightMap.getOrDefault(vessel, 0L);
	}

	@Override
	public void setCountForVessel(IVesselCharter vessel, int count) {
		vesselCountMap.put(vessel, count);
	}

	@Override
	public void setWeightForVessel(IVesselCharter vessel, long weight) {
		vesselWeightMap.put(vessel, weight);
	}

	@Override
	public void setValuesForVessel(IVesselCharter vessel, int count, long weight) {
		setCountForVessel(vessel, count);
		setWeightForVessel(vessel, weight);
	}

}
