/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser;

public interface ILightWeightFitnessFunctionFactory {
	String getName();
	ILightWeightFitnessFunction createFitnessFunction();
}
