/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.fitnessfunctions;

import java.util.List;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightFitnessFunction;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightOptimisationData;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl.CargoWindowData;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl.LightWeightCargoDetails;
import com.mmxlabs.optimiser.common.components.ITimeWindow;

public class VesselCargoCountLightWeightFitnessFunction implements ILightWeightFitnessFunction {
	@Inject
	ILightWeightOptimisationData lightWeightOptimisationData;
	
	public long[] perVesselWeight;
	
	@Override
	public long evaluate(List<List<Integer>> sequences) {
		long fitness = 0L;
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
		perVesselWeight = new long[lightWeightOptimisationData.getVessels().size()];
		for (int i = 0; i < lightWeightOptimisationData.getVessels().size(); i++) {
			if (lightWeightOptimisationData.getDesiredVesselCargoCount()[i] > 0) {
				perVesselWeight[i] = lightWeightOptimisationData.getDesiredVesselCargoWeight()[i];
			}
		}
	}

	@Override
	public long annotate(List<List<Integer>> sequences) {
		return evaluate(sequences);
	}
}
