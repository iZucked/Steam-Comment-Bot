/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser;

import java.util.List;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public interface ICargoToCargoCostCalculator {
	long[][][] createCargoToCargoCostMatrix(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels);
	int[][] getMinCargoStartToEndSlotTravelTimesPerVessel(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels);
	int[][][] getMinCargoToCargoTravelTimesPerVessel(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels);
	
	/**
	 * Calculates the cargo charter costs.
	 * Only calculates cost for non-optional charters
	 * @param cargoes
	 * @param vessels
	 * @return
	 */
	long[][] getCargoCharterCostPerAvailability(List<List<IPortSlot>> cargoes, List<IVesselAvailability> vessels);
	int[] getCargoStartSlotDurations(List<List<IPortSlot>> cargoes);
	
	int[] getCargoEndSlotDurations(List<List<IPortSlot>> cargoes);
}
