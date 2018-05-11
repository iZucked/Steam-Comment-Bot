package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.constraints;

import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ILightWeightConstraintChecker;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ILightWeightConstraintCheckerFactory;

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
