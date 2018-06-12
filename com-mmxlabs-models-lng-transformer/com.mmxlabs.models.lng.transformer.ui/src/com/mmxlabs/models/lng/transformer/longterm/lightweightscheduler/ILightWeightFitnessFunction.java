package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

import java.util.List;

import com.mmxlabs.models.lng.transformer.longterm.metaheuristic.TabuLightWeightSequenceOptimiser.Interval;

public interface ILightWeightFitnessFunction {
	void init();
	Double evaluate(List<List<Integer>> sequences, int cargoCount, double[] cargoPNL, double[] vesselCapacities, double[][][] cargoToCargoCostsOnAvailability,
			List<List<Integer>> cargoVesselRestrictions, int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes, Interval[] loads, Interval[] discharges);
}
