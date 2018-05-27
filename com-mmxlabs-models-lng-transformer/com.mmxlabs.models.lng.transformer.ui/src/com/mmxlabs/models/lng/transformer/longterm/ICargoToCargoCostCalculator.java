/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm;

import java.util.List;

import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public interface ICargoToCargoCostCalculator {
	Long[][][] createCargoToCargoCostMatrix(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels);
	long calculateNonCharterVariableCosts(ILoadSlot loadA, IDischargeSlot dischargeA, ILoadSlot loadB, IDischargeSlot dischargeB, IVessel vessel);
	int[][] getMinCargoStartToEndSlotTravelTimesPerVessel(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels);
	int[][][] getMinCargoToCargoTravelTimesPerVessel(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels);
	long calculateCharterInVariableCosts(ILoadSlot loadA, IDischargeSlot dischargeA, ILoadSlot loadB, IDischargeSlot dischargeB, IVesselAvailability vesselAvailability);
}
