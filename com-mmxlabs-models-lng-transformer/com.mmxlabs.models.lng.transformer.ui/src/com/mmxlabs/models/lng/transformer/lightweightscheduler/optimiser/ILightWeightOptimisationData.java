/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl.CargoWindowData;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl.LightWeightCargoDetails;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;

public interface ILightWeightOptimisationData {

	long[] getCargoPNLPerM3();

	/**
	 * Calculates the cargo charter costs. Only calculates cost for non-optional charters
	 * 
	 * @return
	 */
	long[][][] getCargoToCargoCostsOnAvailability();

	List<Set<Integer>> getCargoVesselRestrictions();

	int[][][] getCargoToCargoMinTravelTimes();

	int[][] getCargoMinTravelTimes();

	List<List<IPortSlot>> getShippedCargoes();

	List<List<IPortSlot>> getNonShippedCargoes();

	List<IVesselCharter> getVessels();

	Map<ILoadOption, IDischargeOption> getPairingsMap();

	int[] getDesiredVesselCargoCount();

	long[] getVesselCapacities();

	long[] getDesiredVesselCargoWeight();

	long[] getCargoesVolumesInM3();

	LightWeightCargoDetails[] getCargoDetails();

	long[][] getCargoCharterCostPerAvailability();

	Set<Integer> getCargoIndexes();

	Set<Integer> getEventIndexes();

	ITimeWindow[] getVesselStartWindows();

	ITimeWindow[] getVesselEndWindows();

	int[] getCargoStartSlotDurations();

	int[] getCargoEndSlotDurations();

	CargoWindowData[] getCargoWindows();

	int getCargoCount();
}