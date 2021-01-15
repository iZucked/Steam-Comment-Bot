/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.fitnessfunctions;

import java.util.List;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightFitnessFunction;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightOptimisationData;

public class VesselCargoCountLightWeightFitnessFunction implements ILightWeightFitnessFunction {
	@Inject
	ILightWeightOptimisationData lightWeightOptimisationData;
	
	@Override
	public long evaluate(List<List<Integer>> sequences) {
		long fitness = 0L;
		int idx = 0;
		for (List<Integer> sequence : sequences) {
			int sum = (int) sequence.stream().count();
			fitness += (Math.min(sum,
					lightWeightOptimisationData.getDesiredVesselCargoCount()[idx])
					* lightWeightOptimisationData.getDesiredVesselCargoWeight()[idx]
					);
			idx++;
		}
		return fitness;
	}

	@Override
	public void init() {
		
	}

	@Override
	public long annotate(List<List<Integer>> sequences) {
		return evaluate(sequences);
	}
}
