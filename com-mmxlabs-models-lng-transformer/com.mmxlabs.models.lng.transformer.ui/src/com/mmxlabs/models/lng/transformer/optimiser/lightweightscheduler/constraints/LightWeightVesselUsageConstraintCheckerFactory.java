/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.constraints;

import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.IFullLightWeightConstraintChecker;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.IFullLightWeightConstraintCheckerFactory;

public class LightWeightVesselUsageConstraintCheckerFactory implements IFullLightWeightConstraintCheckerFactory {
	public static final String NAME = "LightWeightVesselUsageConstraintChecker";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IFullLightWeightConstraintChecker createConstraintChecker() {
		return new LightWeightVesselUsageConstraintChecker();
	}

}
