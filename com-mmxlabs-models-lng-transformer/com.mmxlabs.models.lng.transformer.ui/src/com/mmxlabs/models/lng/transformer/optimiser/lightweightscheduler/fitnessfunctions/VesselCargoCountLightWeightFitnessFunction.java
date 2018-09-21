/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.fitnessfunctions;

import java.util.List;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightFitnessFunction;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightOptimisationData;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl.LightWeightCargoDetails;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.sequenceoptimisers.metaheuristic.tabu.CargoWindowData;
import com.mmxlabs.optimiser.common.components.ITimeWindow;

public class VesselCargoCountLightWeightFitnessFunction implements ILightWeightFitnessFunction {
	@Inject
	ILightWeightOptimisationData lightWeightOptimisationData;
	
	public double[] perVesselWeight;
	
	@Override
	public Double evaluate(List<List<Integer>> sequences, int cargoCount, double[] cargoPNL, double[] vesselCapacities, double[][][] cargoToCargoCostsOnAvailability,
			List<List<Integer>> cargoVesselRestrictions, int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes, CargoWindowData[] cargoWindows, double[] volumes,
			LightWeightCargoDetails[] cargoDetails, double[][] cargoDailyCharterCostPerAvailabilityProcessed, ITimeWindow[] vesselStartTimeWindows, ITimeWindow[] vesselEndTimeWindows,
			int[] cargoEndDurations) {
		double fitness = 0.0;
		int idx = 0;
		for (List<Integer> sequence : sequences) {
			int sum = (int) sequence.stream().count();
			fitness += (Math.min(sum, lightWeightOptimisationData.getDesiredVesselCargoCount()[idx])*perVesselWeight[idx]);
			idx++;
		}
		return fitness;
	}

	@Override
	public void init() {
		perVesselWeight = new double[lightWeightOptimisationData.getVessels().size()];
		for (int i = 0; i < lightWeightOptimisationData.getVessels().size(); i++) {
			if (lightWeightOptimisationData.getDesiredVesselCargoCount()[i] > 0) {
				perVesselWeight[i] = lightWeightOptimisationData.getDesiredVesselCargoWeight()[i];
			}
		}
	}

	@Override
	public Double annotate(List<List<Integer>> sequences, int cargoCount, double[] cargoPNL, double[] vesselCapacities, double[][][] cargoToCargoCostsOnAvailability,
			List<List<Integer>> cargoVesselRestrictions, int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes, CargoWindowData[] cargoWindows, double[] volumes,
			LightWeightCargoDetails[] cargoDetails, double[][] cargoDailyCharterCostPerAvailabilityProcessed, ITimeWindow[] vesselStartTimeWindows, ITimeWindow[] vesselEndTimeWindows,
			int[] cargoEndDurations) {
		return evaluate(sequences, cargoCount, cargoPNL, vesselCapacities, cargoToCargoCostsOnAvailability, cargoVesselRestrictions,
				cargoToCargoMinTravelTimes, cargoMinTravelTimes, cargoWindows, volumes, cargoDetails, cargoDailyCharterCostPerAvailabilityProcessed, vesselStartTimeWindows, vesselEndTimeWindows, cargoEndDurations);
	}
}
