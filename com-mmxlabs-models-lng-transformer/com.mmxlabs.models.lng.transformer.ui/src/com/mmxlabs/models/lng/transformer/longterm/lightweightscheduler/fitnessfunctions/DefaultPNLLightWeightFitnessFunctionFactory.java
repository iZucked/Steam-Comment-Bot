package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.fitnessfunctions;

import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ILightWeightFitnessFunction;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ILightWeightFitnessFunctionFactory;

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
