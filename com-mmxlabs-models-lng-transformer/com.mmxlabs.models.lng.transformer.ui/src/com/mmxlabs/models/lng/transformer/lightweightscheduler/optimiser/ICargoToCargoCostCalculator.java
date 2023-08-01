/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser;

import java.util.List;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;

public interface ICargoToCargoCostCalculator {
	long[][][] createCargoToCargoCostMatrix(List<List<IPortSlot>> cargoes, List<IVesselCharter> vessels);
	int[][] getMinCargoStartToEndSlotTravelTimesPerVessel(List<List<IPortSlot>> cargoes, List<IVesselCharter> vessels);
	int[][][] getMinCargoToCargoTravelTimesPerVessel(List<List<IPortSlot>> cargoes, List<IVesselCharter> vessels);
	
	/**
	 * Calculates the cargo charter costs.
	 * Only calculates cost for non-optional charters
	 * @param cargoes
	 * @param vessels
	 * @return
	 */
	long[][] getCargoCharterCostPerAvailability(List<List<IPortSlot>> cargoes, List<IVesselCharter> vessels);
	int[] getCargoStartSlotDurations(List<List<IPortSlot>> cargoes);
	
	int[] getCargoEndSlotDurations(List<List<IPortSlot>> cargoes);
	
	int[][] getStartToFirstCargoTravelTimesPerVessel(List<List<IPortSlot>> cargoes, List<IVesselCharter> vessels);
}
