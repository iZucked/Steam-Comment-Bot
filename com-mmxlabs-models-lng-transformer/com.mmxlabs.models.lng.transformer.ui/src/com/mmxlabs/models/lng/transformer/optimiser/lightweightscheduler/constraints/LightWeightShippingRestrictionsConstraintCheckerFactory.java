/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.constraints;

import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightConstraintChecker;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightConstraintCheckerFactory;

public class LightWeightShippingRestrictionsConstraintCheckerFactory implements ILightWeightConstraintCheckerFactory {
	final String NAME = "LightWeightShippingRestrictionsConstraintChecker";
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public ILightWeightConstraintChecker createConstraintChecker() {
		return new LightWeightShippingRestrictionsConstraintChecker();
	}

}
