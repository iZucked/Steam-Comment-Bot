package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.fitnessfunctions;

import java.util.List;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ILightWeightFitnessFunction;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ILightWeightOptimisationData;
import com.mmxlabs.models.lng.transformer.longterm.metaheuristic.TabuLightWeightSequenceOptimiser.Interval;

public class VesselCargoCountLightWeightFitnessFunction implements ILightWeightFitnessFunction {
	@Inject
	ILightWeightOptimisationData lightWeightOptimisationData;
	
	public double[] perVesselWeight;
	
	@Override
	public Double evaluate(List<List<Integer>> sequences, int cargoCount, double[] cargoPNL, double[] vesselCapacities, double[][][] cargoToCargoCostsOnAvailability,
			List<List<Integer>> cargoVesselRestrictions, int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes, Interval[] loads, Interval[] discharges) {
		double fitness = 0.0;
		int idx = 0;
		for (List<Integer> sequence : sequences) {
			int sum = sequence.stream().mapToInt(s->1).sum();
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
}
