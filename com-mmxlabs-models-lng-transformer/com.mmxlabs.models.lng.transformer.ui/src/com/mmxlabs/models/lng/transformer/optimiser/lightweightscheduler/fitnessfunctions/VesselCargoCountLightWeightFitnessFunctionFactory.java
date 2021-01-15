/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.fitnessfunctions;

import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightFitnessFunction;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightFitnessFunctionFactory;

public class VesselCargoCountLightWeightFitnessFunctionFactory implements ILightWeightFitnessFunctionFactory {
	public static final String NAME = "VesselCargoCountLightWeightFitnessFunction";
	
	@Override
	public ILightWeightFitnessFunction createFitnessFunction() {
		return new VesselCargoCountLightWeightFitnessFunction();
	}

	@Override
	public String getName() {
		return this.NAME;
	}

}
