/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.fitnessfunctions;

import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightFitnessFunction;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightFitnessFunctionFactory;

public class DefaultPNLLightWeightFitnessFunctionFactory implements ILightWeightFitnessFunctionFactory {
	public static final String NAME = "DefaultPNLLightWeightFitnessFunction";

	@Override
	public ILightWeightFitnessFunction createFitnessFunction() {
		return new DefaultPNLLightWeightFitnessFunction();
	}

	@Override
	public String getName() {
		return this.NAME;
	}

}
