package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

public interface ILightWeightFitnessFunctionFactory {
	String getName();
	ILightWeightFitnessFunction createFitnessFunction();
}
