package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;


public class DefaultPNLLightWeightFitnessFunctionFactory implements ILightWeightFitnessFunctionFactory {

	@Override
	public ILightWeightFitnessFunction createFitnessFunction() {
		return new DefaultPNLLightWeightFitnessFunction();
	}

}
