package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.fitnessfunctions;

import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.ILightWeightFitnessFunction;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.ILightWeightFitnessFunctionFactory;

public class DefaultPNLLightWeightFitnessFunctionFactory implements ILightWeightFitnessFunctionFactory {
	public final String NAME = "DefaultPNLLightWeightFitnessFunction";

	@Override
	public ILightWeightFitnessFunction createFitnessFunction() {
		return new DefaultPNLLightWeightFitnessFunction();
	}

	@Override
	public String getName() {
		return this.NAME;
	}

}
