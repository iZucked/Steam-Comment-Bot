package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.fitnessfunctions;

import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ILightWeightFitnessFunction;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ILightWeightFitnessFunctionFactory;

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
