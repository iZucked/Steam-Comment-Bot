package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler;

public interface ILightWeightFitnessFunctionFactory {
	String getName();
	ILightWeightFitnessFunction createFitnessFunction();
}
