/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser;

import java.util.List;

import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl.LightWeightCargoDetails;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.sequenceoptimisers.metaheuristic.tabu.CargoWindowData;
import com.mmxlabs.optimiser.common.components.ITimeWindow;

public interface ILightWeightFitnessFunction {
	void init();
	Double evaluate(List<List<Integer>> sequences, int cargoCount, double[] cargoPNL, double[] vesselCapacities, double[][][] cargoToCargoCostsOnAvailability,
			List<List<Integer>> cargoVesselRestrictions, int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes, CargoWindowData[] cargoWindows, double[] volumes,
			LightWeightCargoDetails[] cargoDetails, double[][] cargoDailyCharterCostPerAvailabilityProcessed, ITimeWindow[] vesselStartTimeWindows,
			ITimeWindow[] vesselEndTimeWindows, int[] cargoEndDurations);
	
	Double annotate(List<List<Integer>> sequences, int cargoCount, double[] cargoPNL, double[] vesselCapacities, double[][][] cargoToCargoCostsOnAvailability,
			List<List<Integer>> cargoVesselRestrictions, int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes, CargoWindowData[] cargoWindows, double[] volumes,
			LightWeightCargoDetails[] cargoDetails, double[][] cargoDailyCharterCostPerAvailabilityProcessed, ITimeWindow[] vesselStartTimeWindows,
			ITimeWindow[] vesselEndTimeWindows, int[] cargoEndDurations);

}
