package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;


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
