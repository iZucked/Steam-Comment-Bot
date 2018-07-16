/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.constraints;

import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.ILightWeightConstraintChecker;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.ILightWeightConstraintCheckerFactory;

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
