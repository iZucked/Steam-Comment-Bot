package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public interface ILightWeightOptimisationData {

	long[] getCargoPNL();

	Long[][][] getCargoToCargoCostsOnAvailability();

	List<Set<Integer>> getCargoVesselRestrictions();

	int[][][] getCargoToCargoMinTravelTimes();

	int[][] getCargoMinTravelTimes();

	List<List<IPortSlot>> getCargoes();

	List<IVesselAvailability> getVessels();

	Map<ILoadOption, IDischargeOption> getPairingsMap();

	int[] getDesiredVesselCargoCount();

	double[] getVesselCapacities();

	long[] getDesiredVesselCargoWeight();
	
	double[] getCargoesVolumes();
}