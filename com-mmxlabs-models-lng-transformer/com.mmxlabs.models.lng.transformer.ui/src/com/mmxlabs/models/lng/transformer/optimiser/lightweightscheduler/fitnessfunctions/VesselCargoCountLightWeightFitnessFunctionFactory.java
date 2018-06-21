package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.fitnessfunctions;

import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.ILightWeightFitnessFunction;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.ILightWeightFitnessFunctionFactory;

public class VesselCargoCountLightWeightFitnessFunctionFactory implements ILightWeightFitnessFunctionFactory {
	public final String NAME = "VesselCargoCountLightWeightFitnessFunction";
	
	@Override
	public ILightWeightFitnessFunction createFitnessFunction() {
		return new VesselCargoCountLightWeightFitnessFunction();
	}

	@Override
	public String getName() {
		return this.NAME;
	}

}
